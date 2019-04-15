package sawyern.cookiebot.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import sawyern.cookiebot.exception.RuntimeCookieException;

import java.util.function.Consumer;

@Slf4j
@UtilityClass
public class ThrowingConsumerWrapper {
    public static <T, E extends Exception> Consumer<T> throwingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer
            , Class<E> exceptionClass)
    {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    log.error(exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeCookieException(ex);
                }
            }
        };
    }
}
