package sawyern.cookiebot.commands;

import com.google.common.collect.Sets;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class GroupRollCommand extends MessageCreateEventBotCommand {

    private final CookieService cookieService;

    private int countdown;

    private static int min = 1;
    private static int max = 100;

    @Override
    public String getCommand() {
        return "grouproll";
    }

    @Override
    public Set<Integer> getAllowedNumArgs() {
        return Sets.newHashSet(1);
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        int bet = getBotUtil().parsePositiveIntArgument(args.get(0));
        countdown = 20;

        Message message = getBotUtil().sendMessage(event, getMessageContent(bet));
        message.addReaction(ReactionEmoji.unicode(CommandConstants.DICE));

        startCountdownEditMessage(message, bet);

        Flux<User> reactions = message.getReactors(ReactionEmoji.unicode(CommandConstants.DICE));
        getBotUtil().sendMessage(event, "Starting rolls...");

        StringBuilder builder = new StringBuilder("```");
        Map<Account, Integer> userRollMap = new HashMap<>();

        String selfId = getBotUtil().getSelfId(event);

        reactions.toStream().forEach(user -> {
            if (user.getId().asString().equalsIgnoreCase(selfId))
                return;
            int roll = RollDieCommand.roll(min, max);
            builder.append(user.getUsername()).append(" rolls: ").append(roll).append("\n");
            userRollMap.put(new Account(user.getId().asString(), user.getUsername()), roll);
        });

        if (userRollMap.isEmpty())
            throw new CookieException("No players want to play.");

        Map.Entry<Account, Integer> winner = null;

        for (Map.Entry<Account, Integer> entry : userRollMap.entrySet()) {
            if (cookieService.getAllCookiesForAccount(entry.getKey().getDiscordId()) < bet) {
                getBotUtil().sendMessage(event, "<@" + entry.getKey().getDiscordId() + "> can't pay. Roll not considered.");
                continue;
            }

            if (winner == null || entry.getValue().compareTo(winner.getValue()) > 0) {
                winner = entry;
            }
        }

        if (winner == null)
            throw new CookieException("No eligible winners. Cancelling bet.");

        builder.append("```");

        for (Account account : userRollMap.keySet()) {
            if (cookieService.getAllCookiesForAccount(account.getDiscordId()) < bet)
                continue;
            cookieService.giveCookieTo(
                    GiveCookieDto.builder()
                    .senderId(account.getDiscordId())
                    .recieverId(winner.getKey().getDiscordId())
                    .numCookies(bet)
                    .build()
            );
        }
        getBotUtil().sendMessage(event, builder.toString());
        getBotUtil().sendMessage(event,"Winner <@" + winner.getKey().getDiscordId() + ">!");
    }

    protected void startCountdownEditMessage(Message message, int bet) throws CookieException {
        try {
            while (countdown > 0) {
                TimeUnit.SECONDS.sleep(1);
                final String messageContent = getMessageContent(bet);
                message.edit(m ->  m.setContent(messageContent)).block();
                countdown--;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Thread interrupted.");
        }
    }

    public String getMessageContent(int bet) {
        return "React " + CommandConstants.DICE + " to join. Betting " + bet + " cookies. Starting in " + this.countdown;
    }
}
