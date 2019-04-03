package sawyern.cookiebot.commands;

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
public class MessageCreateEventBotCommandTest {

    @InjectMocks
    private MessageCreateEventBotCommand messageCreateEventBotCommand = new PingCommand();

    @Spy
    private MessageCreateEventBotCommand messageCreateEventBotCommandSpy;

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
        messageCreateEventBotCommand.subscribeCommand(null);
    }

    @Test
    public void subscribeCommand() {
        Mockito.when(discordClient.getEventDispatcher()).thenReturn(eventDispatcher);
        Mockito.when(eventDispatcher.on(Mockito.eq(MessageCreateEvent.class))).thenReturn(messageCreateEventFlux);

        messageCreateEventBotCommand.subscribeCommand(discordClient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeCommandIllegalArgument() {
        messageCreateEventBotCommand.executeCommand(null);
    }

    @Test
    public void executeCommandExecuteGivenInvalidMessageThenThrowsDiscordException() {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.empty());
        messageCreateEventBotCommand.executeCommand(messageCreateEvent);
        Mockito.verify(message).getContent();
    }

    @Test
    public void executeCommandExecuteGivenInvalidCommandThrowsDiscordException() {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("@ping"));
        messageCreateEventBotCommand.executeCommand(messageCreateEvent);
        Mockito.verify(message).getContent();
    }

    @Test
    public void executeCommandInvalidNumArgs() throws CookieException {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));


        Mockito.doReturn("ping").when(messageCreateEventBotCommandSpy).parseCommand(Mockito.eq("!ping arg1"));
        Mockito.doReturn(new ArrayList<>()).when(messageCreateEventBotCommandSpy).parseArgs(Mockito.eq("!ping arg1"));
        Mockito.doReturn("ping").when(messageCreateEventBotCommandSpy).getCommand();
        Mockito.doThrow(new InvalidCommandArgumentLengthCookieException(""))
                .when(messageCreateEventBotCommandSpy).checkValidNumArgs(Mockito.eq(Collections.singletonList("arg1)")));
        messageCreateEventBotCommandSpy.executeCommand(messageCreateEvent);
    }

    @Test
    public void executeCommandSuccess() {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));


        Mockito.doReturn("ping").when(messageCreateEventBotCommandSpy).parseCommand(Mockito.eq("!ping"));
        Mockito.doReturn(new ArrayList<>()).when(messageCreateEventBotCommandSpy).parseArgs(Mockito.eq("!ping"));
        Mockito.doReturn("ping").when(messageCreateEventBotCommandSpy).getCommand();
        messageCreateEventBotCommandSpy.executeCommand(messageCreateEvent);
        Mockito.verify(messageCreateEventBotCommandSpy, Mockito.atLeastOnce()).getCommand();
    }

    @Test
    public void executeCommandCookieException() throws CookieException {
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));

        Mockito.doReturn("ping").when(messageCreateEventBotCommandSpy).parseCommand(Mockito.eq("!ping"));
        Mockito.doReturn(new ArrayList<>()).when(messageCreateEventBotCommandSpy).parseArgs(Mockito.eq("!ping"));
        Mockito.doReturn("ping").when(messageCreateEventBotCommandSpy).getCommand();
        Mockito.doThrow(new CookieException()).when(messageCreateEventBotCommandSpy).execute(Mockito.any(), Mockito.eq(new ArrayList<>()));

        messageCreateEventBotCommandSpy.executeCommand(messageCreateEvent);
        Mockito.verify(messageCreateEventBotCommandSpy, Mockito.atLeastOnce()).getCommand();
    }

    @Test
    public void executeUnexpectedException() {
        Mockito.when(messageCreateEvent.getMessage()).thenThrow(new RuntimeException());
        messageCreateEventBotCommand.executeCommand(messageCreateEvent);
        Mockito.verify(messageCreateEvent, Mockito.atLeastOnce()).getMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseCommandGivenNullMessageThenThrowsIllegalArgumentException() {
        messageCreateEventBotCommand.parseCommand(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseCommandGivenEmptyMessageThenThrowsIllegalArgumentException() {
        messageCreateEventBotCommand.parseCommand("");
    }

    @Test
    public void parseCommand() {
        String message = "!ping";
        String actual = messageCreateEventBotCommand.parseCommand(message);
        Assert.assertEquals("ping", actual);
    }

    @Test
    public void parseCommandWithArg() {
        String message = "!ping arg";
        String actual = messageCreateEventBotCommand.parseCommand(message);
        Assert.assertEquals("ping", actual);
    }

    @Test
    public void parseCommandWithQuoteArgs() {
        String message = "!ping arg1 \"arg2 arg3\"";
        String actual = messageCreateEventBotCommand.parseCommand(message);
        Assert.assertEquals("ping", actual);
    }

    @Test
    public void parseCommandWithWrongStart() {
        String message = "@ping";
        String actual = messageCreateEventBotCommand.parseCommand(message);
        Assert.assertEquals("unknown", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseArgsGivenNullMessageThenThrowsIllegalArgumentException () {
        String message = null;
        List<String> actual = messageCreateEventBotCommand.parseArgs(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseArgsGivenEmptyMessageThenThrowsIllegalArgumentException () {
        String message = "";
        List<String> actual = messageCreateEventBotCommand.parseArgs(message);
    }

    @Test
    public void parseArgs0Args() {
        String message = "!ping";
        List<String> actual = messageCreateEventBotCommand.parseArgs(message);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void parseArgs1Args() {
        String message = "!ping arg1";
        List<String> actual = messageCreateEventBotCommand.parseArgs(message);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals("arg1", actual.get(0));
    }

    @Test
    public void parseArgs2Args() {
        String message = "!ping arg1 arg2";
        List<String> actual = messageCreateEventBotCommand.parseArgs(message);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals("arg2", actual.get(1));
    }

    @Test
    public void parseArgs2ArgsQuote() {
        String message = "!ping arg1 \"arg2 arg3\"";
        List<String> actual = messageCreateEventBotCommand.parseArgs(message);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals("arg2 arg3", actual.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidNumArgsGivenNullArgsThenThrowsIllegalArgumentException() throws CookieException {
        List<String> args = null;
        messageCreateEventBotCommand.checkValidNumArgs(args);
    }

    @Test
    public void checkValidNumArgsWhenGetAllowedNumArgsIsEmpty() throws CookieException {
        Mockito.when(messageCreateEventBotCommandSpy.getAllowedNumArgs()).thenReturn(Collections.emptySet());
        List<String> args = new ArrayList<>();
        messageCreateEventBotCommandSpy.checkValidNumArgs(args);
        Mockito.verify(messageCreateEventBotCommandSpy).getAllowedNumArgs();
    }

    @Test(expected = InvalidCommandArgumentLengthCookieException.class)
    public void checkValidNumArgsWhenGetAllowedNumArgsIsNotEmptyAndNotContainsThenThrowException() throws CookieException {
        Mockito.when(messageCreateEventBotCommandSpy.getAllowedNumArgs()).thenReturn(Sets.newSet(1));
        List<String> args = Arrays.asList("arg1", "arg2");
        messageCreateEventBotCommandSpy.checkValidNumArgs(args);
        Mockito.verify(messageCreateEventBotCommandSpy).getAllowedNumArgs();
    }

    @Test
    public void checkValidNumArgsWhenGetAllowedNumArgsIsNotEmptyAndContains() throws CookieException {
        Mockito.when(messageCreateEventBotCommandSpy.getAllowedNumArgs()).thenReturn(Sets.newSet(1));
        List<String> args = Collections.singletonList("arg1");
        messageCreateEventBotCommandSpy.checkValidNumArgs(args);
        Mockito.verify(messageCreateEventBotCommandSpy, Mockito.atLeastOnce()).getAllowedNumArgs();
    }

    @Test
    public void getCommand() {
        String command = messageCreateEventBotCommand.getCommand();
        Assert.assertEquals("ping", command);
    }
}