package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.util.BotUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelpCommand extends MessageCreateEventBotCommand {
    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) {
        List<String> helpStrings = new ArrayList<>();
        helpStrings.add("!register -- registers your account");
        helpStrings.add("!cookies -- returns how many cookies you have");
        helpStrings.add("!cookies {username} -- returns how many cookies that user has");
        helpStrings.add("!givecookie {num} {username} -- gives num cookies to username");
        helpStrings.add("!leaderboard -- show all cookie count");
        helpStrings.add("!roll {maxNum} -- roll a random number between 0 and maxNum");
        helpStrings.add("!grouproll {bet} -- initiates group automated gambling");
        helpStrings.add("!lootbox -- pay one cookie for a chance at more.");
        helpStrings.add("!odds -- show odds of lootbox");
        helpStrings.add("!weeklytokens -- get weekly tokens for the week");
        helpStrings.add("!tokens -- returns how many tokens you have");

        StringBuilder helpString = new StringBuilder("```");
        for (String str : helpStrings) {
            helpString.append(str);
            helpString.append("\n");
        }
        helpString.append("```");
        BotUtil.sendMessage(event, helpString.toString());
    }
}
