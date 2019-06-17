package sawyern.cookiebot.util;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.InvalidNumberParamCookieException;
import sawyern.cookiebot.services.DiscordService;

@Slf4j
@Service
public class BotUtil {

    private DiscordService discordService;

    public Message sendMessage(String message) {
        return discordService.getBotChannel().createMessage(message).block();
    }

    public Message sendMessage(MessageCreateEvent event, String message) throws CookieException {
        Message messageObj = null;
        try {
            messageObj = event.getMessage().getChannel().block().createMessage(message).block();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Error sending message.");
        }
        return messageObj;
    }

    public String getIdFromUser(MessageCreateEvent event, String username) throws CookieException  {
        User user = event.getClient().getUsers()
                .toStream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst().orElseThrow(() -> new CookieException("Error getting user " + username));
        return user.getId().asString();
    }

    public Member getMember(MessageCreateEvent event) throws CookieException {
        return event.getMember().orElseThrow(() -> new CookieException("Error getting member."));
    }

    public String getSelfId(MessageCreateEvent event) throws CookieException {
        return event.getClient().getSelfId().orElseThrow(() -> new CookieException("Error getting commands id")).asString();
    }

    public int parseIntArgument(String arg) throws CookieException {
        int parsedArg;
        try {
            parsedArg = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new InvalidNumberParamCookieException();
        }
        return parsedArg;
    }

    public int parsePositiveIntArgument(String arg) throws CookieException {
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

    @Autowired
    public void setDiscordService(DiscordService discordService) {
        this.discordService = discordService;
    }
}
