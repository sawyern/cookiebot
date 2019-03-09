package sawyern.cookiebot.util;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sawyern.cookiebot.bot.exception.CookieException;

public class BotUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(BotUtil.class);

    public static Message sendMessage(MessageCreateEvent event, String message) {
        Message messageObj = null;
        try {
            messageObj = event.getMessage().getChannel().block().createMessage(message).block();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return messageObj;
    }


    public static String getIdFromUser(MessageCreateEvent event, String username) throws CookieException  {
        User user = event.getClient().getUsers()
                .toStream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst().orElseThrow(() -> new CookieException("Error getting user " + username));
        return user.getId().asString();
    }

    public static Member getMember(MessageCreateEvent event) throws CookieException {
        return event.getMember().orElseThrow(() -> new CookieException("Error getting member."));
    }

    public static String getSelfId(MessageCreateEvent event) throws CookieException {
        return event.getClient().getSelfId().orElseThrow(() -> new CookieException("Error getting bot id")).asString();
    }
}
