package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.exception.CookieException;

@Component
public class RollDieCommand extends GenericBotCommand {
    @Override
    public String getCommand() {
        return "roll";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        if (getArgs().size() != 2)
            throw new CookieException("Invalid arguments. !roll {maxNum}");
        int maxNum;
        int minNum = 0;
        try {
            maxNum = Integer.parseInt(getArgs().get(1));
        } catch (NumberFormatException e) {
            throw new CookieException("Invalid number argument.");
        }

        if (maxNum <= 0)
            throw new CookieException("Invalid number argument.");

        Integer roll = (int)(Math.random() * ((maxNum - minNum) + 1) + minNum);

        sendMessage (event, roll.toString());
    }
}
