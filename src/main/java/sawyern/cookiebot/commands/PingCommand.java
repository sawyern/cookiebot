package sawyern.cookiebot.commands;

import com.google.common.collect.Sets;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.util.BotUtil;

import java.util.List;
import java.util.Set;

@Component
public class PingCommand extends GenericBotCommand {
    @Override
    public void execute(MessageCreateEvent event, List<String> args)  {
        BotUtil.sendMessage(event, "pong!");
    }

    @Override
    public Set<Integer> getAllowedNumArgs() {
        return Sets.newHashSet(0, 1);
    }

    @Override
    public String getCommand() {
        return CommandConstants.CommandName.PING;
    }
}
