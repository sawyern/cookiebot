package sawyern.cookiebot.bot;

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
import sawyern.cookiebot.util.BotUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class GroupRollCommand extends GenericBotCommand {

    private final CookieService cookieService;

    private int countdown;

    private static int min = 1;
    private static int max = 100;

    @Override
    public String getCommand() {
        return "grouproll";
    }

    @Override
    public List<Integer> allowedNumArgs() {
        return Collections.singletonList(1);
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        int bet = BotUtil.parseIntArgument(getArgs().get(1));
        countdown = 20;

        Message message = BotUtil.sendMessage(event, getMessageContent());
        message.addReaction(ReactionEmoji.unicode(CommandConstants.DICE));

        startCountdownEditMessage(message);

        Flux<User> reactions = message.getReactors(ReactionEmoji.unicode(CommandConstants.DICE));
        BotUtil.sendMessage(event, "Starting rolls...");

        StringBuilder builder = new StringBuilder("```");
        Map<Account, Integer> userRollMap = new HashMap<>();

        String selfId = BotUtil.getSelfId(event);

        reactions.toStream().forEach(user -> {
            if (user.getId().asString().equalsIgnoreCase(selfId))
                return;
            int roll = RollDieCommand.roll(min, max);
            builder.append(user.getUsername() + " rolls: " + roll + "\n");
            userRollMap.put(new Account(user.getId().asString(), user.getUsername()), roll);
        });

        if (userRollMap.isEmpty())
            throw new CookieException("No players want to play.");
        Map.Entry<Account, Integer> winner = null;

        for (Map.Entry<Account, Integer> entry : userRollMap.entrySet()) {
            if (cookieService.getAllCookiesForAccount(entry.getKey().getDiscordId()) < bet) {
                BotUtil.sendMessage(event, "<@" + entry.getKey().getDiscordId() + "> can't pay. Roll not considered.");
                continue;
            }

            if (winner == null) {
                winner = entry;
            }

            else if (entry.getValue().compareTo(winner.getValue()) > 0) {
                winner = entry;
            }

            else if (entry.getValue().equals(winner.getValue()) && RollDieCommand.roll(0, 1) == 1)
                    winner = entry;
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
        BotUtil.sendMessage(event, builder.toString());
        BotUtil.sendMessage(event,"Winner <@" + winner.getKey().getDiscordId() + ">!");
    }

    protected void startCountdownEditMessage(Message message) throws CookieException {
        try {
            while (countdown > 0) {
                TimeUnit.SECONDS.sleep(1);
                final String messageContent = getMessageContent();
                message.edit(m ->  m.setContent(messageContent)).block();
                countdown--;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Thread interrupted.");
        }
    }

    public String getMessageContent() throws CookieException {
        return "React " + CommandConstants.DICE + " to join. Betting " + getArgs().get(1) + " cookies. Starting in " + this.countdown;
    }
}
