package sawyern.cookiebot.commands;

import com.google.common.collect.Sets;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.properties.AppProperties;

import java.util.List;
import java.util.Set;

@Component
public class PingCommand extends MessageCreateEventBotCommand {

    @Autowired
    private AppProperties appProperties;

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        getBotUtil().sendMessage(event, "pong! v" + appProperties.getVersion());
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
