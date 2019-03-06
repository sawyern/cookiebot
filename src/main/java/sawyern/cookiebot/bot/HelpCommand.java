package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.exception.CookieException;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelpCommand extends GenericBotCommand {
    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        List<String> helpStrings = new ArrayList<>();
        helpStrings.add("!register -- registers your account");
        helpStrings.add("!cookies -- returns how many cookies you have");
        helpStrings.add("!cookies {username} -- returns how many cookies that user has");
        helpStrings.add("!givecookie {num} {username} -- gives num cookies to username");
        helpStrings.add("!leaderboard -- show all cookie count");
        helpStrings.add("!roll {maxNum} -- roll a random number between 0 and maxNum");

        StringBuilder helpString = new StringBuilder("```");
        for (String str : helpStrings) {
            helpString.append(str);
            helpString.append("\n");
        }
        helpString.append("```");
        sendMessage(event, helpString.toString());
    }
}
