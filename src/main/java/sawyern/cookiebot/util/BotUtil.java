package sawyern.cookiebot.util;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.InvalidNumberParamCookieException;

@Slf4j
@UtilityClass
public class BotUtil {
    public static Message sendMessage(MessageCreateEvent event, String message) {
        Message messageObj = null;
        try {
            messageObj = event.getMessage().getChannel().block().createMessage(message).block();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        return event.getClient().getSelfId().orElseThrow(() -> new CookieException("Error getting commands id")).asString();
    }

    public static int parseIntArgument(String arg) throws CookieException {
        int parsedArg;
        try {
            parsedArg = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new InvalidNumberParamCookieException();
        }
        return parsedArg;
    }

    public static int parsePositiveIntArgument(String arg) throws CookieException {
        int parsedArg;
        try {
            parsedArg = Integer.parseInt(arg);
            if (parsedArg <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new InvalidNumberParamCookieException();
        }
        return parsedArg;
    }
}
