package sawyern.cookiebot.commands;

import com.google.common.collect.Sets;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.util.BotUtil;

import java.util.List;
import java.util.Set;


@Component
public class RollDieCommand extends MessageCreateEventBotCommand {

    @Override
    public String getCommand() {
        return "roll";
    }

    @Override
    public Set<Integer> getAllowedNumArgs() {
        return Sets.newHashSet(1);
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        int maxNum;
        int minNum = 1;
        try {
            maxNum = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            throw new CookieException("Invalid number argument.");
        }

        if (maxNum <= 0)
            throw new CookieException("Invalid number argument.");

        BotUtil.sendMessage(event, BotUtil.getMember(event).getUsername() + " rolls: " + RollDieCommand.roll(minNum, maxNum).toString());
    }

    public static Integer roll(int min, int max) {
        return CommandConstants.RANDOM.nextInt(max) + min;
    }
}
