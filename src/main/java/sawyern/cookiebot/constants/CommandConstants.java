package sawyern.cookiebot.constants;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class CommandConstants {

    @UtilityClass
    public class CommandName {
        public static final String PING = "ping";
        public static final String REGISTER = "register";
        public static final String COOKIES = "cookies";

        public static final String UNKNOWN = "unknown";
    }

    public static final String COMMAND_START = "!";
    public static final String SPACE = " ";

    public static final Random RANDOM = new Random();

    public static final String COMMAND_DELIM = " ";

    public static final String QUOTE = "\"";
    public static final String EMPTY_STRING = "";

    public static final String QUOTE_REGEX = "([^\"]\\S*|\".+?\")\\s*";
    public static final String DICE = "\uD83C\uDFB2";
}
