package sawyern.cookiebot.util;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import sawyern.cookiebot.models.exception.CookieException;

public class BotUtil {
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
