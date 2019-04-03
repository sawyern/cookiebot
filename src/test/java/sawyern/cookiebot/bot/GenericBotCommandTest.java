package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.InvalidCommandArgumentLengthCookieException;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class GenericBotCommandTest {

    @InjectMocks
    private GenericBotCommand genericBotCommand = new PingCommand();

    @Spy
    private GenericBotCommand genericBotCommandSpy;

    @Mock
    private DiscordClient discordClient;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private Flux<MessageCreateEvent> messageCreateEventFlux;
    @Mock
    private MessageCreateEvent messageCreateEvent;
    @Mock
    private Message message;

    @Test(expected = IllegalArgumentException.class)
    public void subscribeCommandNullParam() {
        genericBotCommand.subscribeCommand(null);
    }

    @Test
    public void subscribeCommand() {
        Mockito.when(discordClient.getEventDispatcher()).thenReturn(eventDispatcher);
        Mockito.when(eventDispatcher.on(Mockito.eq(MessageCreateEvent.class))).thenReturn(messageCreateEventFlux);

        genericBotCommand.subscribeCommand(discordClient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeCommandIllegalArgument() {
        genericBotCommand.executeCommand(null);
    }

    @Test
    public void executeCommandExecuteGivenInvalidMessageThenThrowsDiscordException() {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.empty());
        genericBotCommand.executeCommand(messageCreateEvent);
        Mockito.verify(message).getContent();
    }

    @Test
    public void executeCommandExecuteGivenInvalidCommandThrowsDiscordException() {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("@ping"));
        genericBotCommand.executeCommand(messageCreateEvent);
        Mockito.verify(message).getContent();
    }

    @Test
    public void executeCommandInvalidNumArgs() throws CookieException {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));


        Mockito.doReturn("ping").when(genericBotCommandSpy).parseCommand(Mockito.eq("!ping arg1"));
        Mockito.doReturn(new ArrayList<>()).when(genericBotCommandSpy).parseArgs(Mockito.eq("!ping arg1"));
        Mockito.doReturn("ping").when(genericBotCommandSpy).getCommand();
        Mockito.doThrow(new InvalidCommandArgumentLengthCookieException(""))
                .when(genericBotCommandSpy).checkValidNumArgs(Mockito.eq(Collections.singletonList("arg1)")));
        genericBotCommandSpy.executeCommand(messageCreateEvent);
    }

    @Test
    public void executeCommandSuccess() {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));


        Mockito.doReturn("ping").when(genericBotCommandSpy).parseCommand(Mockito.eq("!ping"));
        Mockito.doReturn(new ArrayList<>()).when(genericBotCommandSpy).parseArgs(Mockito.eq("!ping"));
        Mockito.doReturn("ping").when(genericBotCommandSpy).getCommand();
        genericBotCommandSpy.executeCommand(messageCreateEvent);
        Mockito.verify(genericBotCommandSpy).getCommand();
    }

    @Test
    public void executeCommandCookieException() throws CookieException {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));

        Mockito.doReturn("ping").when(genericBotCommandSpy).parseCommand(Mockito.eq("!ping"));
        Mockito.doReturn(new ArrayList<>()).when(genericBotCommandSpy).parseArgs(Mockito.eq("!ping"));
        Mockito.doReturn("ping").when(genericBotCommandSpy).getCommand();
        Mockito.doThrow(new CookieException()).when(genericBotCommandSpy).execute(Mockito.any(), Mockito.eq(new ArrayList<>()));

        genericBotCommandSpy.executeCommand(messageCreateEvent);
        Mockito.verify(genericBotCommandSpy).getCommand();
    }

    @Test
    public void executeUnexpectedException() {
        Mockito.when(messageCreateEvent.getMessage()).thenThrow(new RuntimeException());
        genericBotCommand.executeCommand(messageCreateEvent);
        Mockito.verify(genericBotCommandSpy).executeCommand(Mockito.eq(messageCreateEvent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseCommandGivenNullMessageThenThrowsIllegalArgumentException() {
        genericBotCommand.parseCommand(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseCommandGivenEmptyMessageThenThrowsIllegalArgumentException() {
        genericBotCommand.parseCommand("");
    }

    @Test
    public void parseCommand() {
        String message = "!ping";
        String actual = genericBotCommand.parseCommand(message);
        Assert.assertEquals("ping", actual);
    }

    @Test
    public void parseCommandWithArg() {
        String message = "!ping arg";
        String actual = genericBotCommand.parseCommand(message);
        Assert.assertEquals("ping", actual);
    }

    @Test
    public void parseCommandWithQuoteArgs() {
        String message = "!ping arg1 \"arg2 arg3\"";
        String actual = genericBotCommand.parseCommand(message);
        Assert.assertEquals("ping", actual);
    }

    @Test
    public void parseCommandWithWrongStart() {
        String message = "@ping";
        String actual = genericBotCommand.parseCommand(message);
        Assert.assertEquals("unknown", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseArgsGivenNullMessageThenThrowsIllegalArgumentException () {
        String message = null;
        List<String> actual = genericBotCommand.parseArgs(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseArgsGivenEmptyMessageThenThrowsIllegalArgumentException () {
        String message = "";
        List<String> actual = genericBotCommand.parseArgs(message);
    }

    @Test
    public void parseArgs0Args() {
        String message = "!ping";
        List<String> actual = genericBotCommand.parseArgs(message);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void parseArgs1Args() {
        String message = "!ping arg1";
        List<String> actual = genericBotCommand.parseArgs(message);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals("arg1", actual.get(0));
    }

    @Test
    public void parseArgs2Args() {
        String message = "!ping arg1 arg2";
        List<String> actual = genericBotCommand.parseArgs(message);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals("arg2", actual.get(1));
    }

    @Test
    public void parseArgs2ArgsQuote() {
        String message = "!ping arg1 \"arg2 arg3\"";
        List<String> actual = genericBotCommand.parseArgs(message);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals("arg2 arg3", actual.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidNumArgsGivenNullArgsThenThrowsIllegalArgumentException() throws CookieException {
        List<String> args = null;
        genericBotCommand.checkValidNumArgs(args);
    }

    @Test
    public void checkValidNumArgsWhenGetAllowedNumArgsIsEmpty() throws CookieException {
        Mockito.when(genericBotCommandSpy.getAllowedNumArgs()).thenReturn(Collections.emptySet());
        List<String> args = new ArrayList<>();
        genericBotCommandSpy.checkValidNumArgs(args);
        Mockito.verify(genericBotCommandSpy).getAllowedNumArgs();
    }

    @Test(expected = InvalidCommandArgumentLengthCookieException.class)
    public void checkValidNumArgsWhenGetAllowedNumArgsIsNotEmptyAndNotContainsThenThrowException() throws CookieException {
        Mockito.when(genericBotCommandSpy.getAllowedNumArgs()).thenReturn(Sets.newSet(1));
        List<String> args = Arrays.asList("arg1", "arg2");
        genericBotCommandSpy.checkValidNumArgs(args);
        Mockito.verify(genericBotCommandSpy).getAllowedNumArgs();
    }

    @Test
    public void checkValidNumArgsWhenGetAllowedNumArgsIsNotEmptyAndContains() throws CookieException {
        Mockito.when(genericBotCommandSpy.getAllowedNumArgs()).thenReturn(Sets.newSet(1));
        List<String> args = Collections.singletonList("arg1");
        genericBotCommandSpy.checkValidNumArgs(args);
        Mockito.verify(genericBotCommandSpy, Mockito.atLeastOnce()).getAllowedNumArgs();
    }

    @Test
    public void getCommand() {
        String command = genericBotCommand.getCommand();
        Assert.assertEquals("ping", command);
    }
}