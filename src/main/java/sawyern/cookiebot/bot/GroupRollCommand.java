package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.bot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class GroupRollCommand extends GenericBotCommand {

    private int countdown;
    private CookieService cookieService;
    private Logger LOGGER = LoggerFactory.getLogger(GroupRollCommand.class);

    private static int min = 1;
    private static int max = 100;

    @Override
    public String getCommand() {
        return "grouproll";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        if (getArgs().size() != 2)
            throw new CookieException("Invalid arguments. !grouproll {bet}");

        Integer bet;

        try {
            bet = Integer.parseInt(getArgs().get(1));
        } catch (NumberFormatException e) {
            throw new CookieException("Invalid parameter.");
        }

        countdown = 20;

        Message message = BotUtil.sendMessage(event, getMessageContent());
        message.addReaction(ReactionEmoji.unicode(CommandConstants.DICE));

        try {
            while (countdown > 0) {
                TimeUnit.SECONDS.sleep(1);
                message.edit(m ->  m.setContent(getMessageContent())).block();
                countdown--;
            }
        } catch (InterruptedException e) {
            throw new CookieException("Interrupted exception.");
        }

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

            else if (entry.getValue().equals(winner.getValue()))
                if (RollDieCommand.roll(0, 1) == 1)
                    winner = entry;
        }

        if (winner == null)
            throw new CookieException("No eligible winners. Cancelling bet.");

        builder.append("```");

        for (Account account : userRollMap.keySet()) {
            cookieService.giveCookieTo(new GiveCookieDto(account.getDiscordId(), winner.getKey().getDiscordId(), bet));
        }
        BotUtil.sendMessage(event, builder.toString());
        BotUtil.sendMessage(event,"Winner <@" + winner.getKey().getDiscordId() + ">!");
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    public String getMessageContent() {
        return "React " + CommandConstants.DICE + " to join. Betting " + getArgs().get(1) + " cookies. Starting in " + this.countdown;
    }
}
