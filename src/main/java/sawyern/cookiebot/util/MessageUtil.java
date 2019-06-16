package sawyern.cookiebot.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.exception.AssertMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class MessageUtil {
    /**
     * splits the message by " " (space) into a list of string arguments.
     * if argument is surrounded by quotes, it will consider the block as one argument
     *
     * ex. input -> this is a message
     *     output -> ["this", "is", "a", "message"]
     *
     *     input -> this is "a message"
     *     output -> ["this", "is", "a message"]
     *
     * @param message input message
     */
    public static List<String> splitMessage(@NonNull String message) {
        Assert.isTrue(!message.isEmpty(), AssertMessage.EMPTY_MESSAGE);

        List<String> msgArgs = new ArrayList<>();

        // split string by space, group ""
        Matcher m = Pattern.compile(CommandConstants.QUOTE_REGEX).matcher(message);
        while (m.find())
            msgArgs.add(m.group(1).replace(CommandConstants.QUOTE, CommandConstants.EMPTY_STRING));
        return msgArgs;
    }
}
