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
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.entity.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiffRollCommand extends GroupRollCommand {

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
            int roll = RollDieCommand.roll(min, bet);
            builder.append(user.getUsername()).append(" rolls: ").append(roll).append("\n");
            userRollMap.put(new Account(user.getId().asString(), user.getUsername()), roll);
        });

        if (userRollMap.isEmpty())
            throw new CookieException("No players want to play.");

        Map.Entry<Account, Integer> winner = null;
        Map.Entry<Account, Integer> loser = null;

        for (Map.Entry<Account, Integer> entry : userRollMap.entrySet()) {
            if (cookieService.getAllCookiesForAccount(entry.getKey().getDiscordId()) < bet) {
                getBotUtil().sendMessage(event, "<@" + entry.getKey().getDiscordId() + "> can't pay. Roll not considered.");
                continue;
            }

            if (winner == null || entry.getValue().compareTo(winner.getValue()) > 0) {
                winner = entry;
            }

            if (loser == null || entry.getValue().compareTo(loser.getValue()) < 0) {
                loser = entry;
            }
        }

        if (winner == null || loser.getKey().getDiscordId().equals(winner.getKey().getDiscordId()))
            throw new CookieException("No eligible winners. Cancelling bet.");

        if (winner.getValue().equals(loser.getValue()))
            throw new CookieException("Tie. No winners. Cancelling bet.");

        builder.append("```");

        cookieService.giveCookieTo(
                GiveCookieDto.builder()
                        .senderId(loser.getKey().getDiscordId())
                        .recieverId(winner.getKey().getDiscordId())
                        .numCookies(winner.getValue() - loser.getValue())
                        .build());

        getBotUtil().sendMessage(event, builder.toString());
        getBotUtil().sendMessage(event,"Winner <@" + winner.getKey().getDiscordId() + ">!");

    }

    @Override
    public String getMessageContent(int bet) {
        return "-- DIFF ROLL -- Lowest roll pays highest roll the difference of their rolls. Ties are cancelled. Other participants lose or win nothing. React " + CommandConstants.DICE + " to join. Starting in " + this.countdown;
    }

    @Override
    public String getCommand() {
        return "diffroll";
    }

    @Override
    public Set<Integer> getAllowedNumArgs() {
        return Sets.newHashSet(1);
    }
}
