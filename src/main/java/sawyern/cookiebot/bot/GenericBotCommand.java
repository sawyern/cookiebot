package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.models.exception.CookieException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GenericBotCommand implements BotCommand {
    private List<String> args;

    @Override
    public void subscribeCommand(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    try {
                        if (parseCommand(event.getMessage().getContent()
                                .orElseThrow(() -> new CookieException("")))
                        )
                            execute(event);
                    } catch (Exception e) {
                        sendMessage(event, e.getMessage());
                    }
                });
    }

    public abstract String getCommand();

    public boolean parseCommand(String message) {
        args = parseArgs(message);
        String command = args.stream().findFirst().orElse("");
        if (command.startsWith(CommandConstants.COMMAND_START)) {
            return command.substring(1).equalsIgnoreCase(getCommand());
        } else return false;
    }

    public List<String> parseArgs(String message) {
        List<String> msgArgs = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message);
        while (m.find())
            msgArgs.add(m.group(1).replace("\"", ""));
        return msgArgs;
    }

    public void sendMessage(MessageCreateEvent event, String message) {
        event.getMessage().getChannel().block().createMessage(message).block();
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
