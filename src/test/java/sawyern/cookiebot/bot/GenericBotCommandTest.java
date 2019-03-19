package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.exception.ArgsNotParsedCookieException;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.InvalidCommandArgumentLengthCookieException;
import sawyern.cookiebot.exception.InvalidMessageCookieException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
public class GenericBotCommandTest {

    private GenericBotCommand genericBotCommand;

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

    @Before
    public void setup() throws CookieException {
        genericBotCommand = new PingCommand();
        Mockito.when(discordClient.getEventDispatcher()).thenReturn(eventDispatcher);
        Mockito.when(eventDispatcher.on(Mockito.eq(MessageCreateEvent.class))).thenReturn(messageCreateEventFlux);
        Mockito.when(messageCreateEvent.getMessage()).thenReturn(message);
        Mockito.when(message.getContent()).thenReturn(Optional.of("!ping"));
    }

    @Test
    public void getCommand() {
        String command = genericBotCommand.getCommand();
        Assert.assertEquals(CommandConstants.PING, command);
    }

    @Test
    public void parseCommandTrue() throws CookieException {
        String testString = "!ping";
        Mockito.doReturn(Arrays.asList("!ping"))
                .when(genericBotCommandSpy).getArgs();
        Mockito.doReturn("ping")
                .when(genericBotCommandSpy).getCommand();
        boolean actual = genericBotCommandSpy.parseCommand(testString);
        Assert.assertTrue(actual);
    }

    @Test
    public void parseCommandFalseWrongCommand() throws CookieException {
        String testString = "!pong";
        Mockito.doReturn(Arrays.asList("!pong"))
                .when(genericBotCommandSpy).getArgs();
        Mockito.doReturn("ping")
                .when(genericBotCommandSpy).getCommand();
        boolean actual = genericBotCommandSpy.parseCommand(testString);
        Assert.assertFalse(actual);
    }

    @Test
    public void parseCommandFalseWrongCommandStart() throws CookieException {
        String testString = "~ping";
        boolean actual = genericBotCommand.parseCommand(testString);
        Assert.assertFalse(actual);
    }

    @Test
    public void parseCommandGivenNullMessage() throws CookieException {
        String testString = null;
        Assert.assertFalse(genericBotCommand.parseCommand(testString));
    }

    @Test
    public void parseCommandGivenEmptyMessage() throws CookieException {
        String testString = "";
        Assert.assertFalse(genericBotCommand.parseCommand(testString));
    }

    @Test
    public void parseCommandGivenDoubleQuotes() throws CookieException {
        String testString = "\"\"\"\"";
        Assert.assertFalse(genericBotCommand.parseCommand(testString));
    }

    @Test
    public void getArgsOneAllowed() throws CookieException {
        genericBotCommand.parseArgs("!ping");
        List<String> args = genericBotCommand.getArgs();
        Assert.assertEquals(1, args.size());
        Assert.assertEquals("!ping", args.get(0));
    }

    @Test
    public void getArgsTwoAllowed() throws CookieException {
        genericBotCommand.parseArgs("!ping arg1");
        List<String> args = genericBotCommand.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("!ping", args.get(0));
        Assert.assertEquals("arg1", args.get(1));
    }

    @Test(expected = ArgsNotParsedCookieException.class)
    public void getArgsNullArgsThenThrowCookieException() throws CookieException {
        genericBotCommand.getArgs();
    }

    @Test
    public void getArgsWithSpace() throws CookieException {
        genericBotCommand.parseArgs("!ping \"arg1 with space\"");
        List<String> args = genericBotCommand.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("arg1 with space", args.get(1));
        Assert.assertEquals("!ping", args.get(0));
    }

    @Test
    public void subscribeCommand() {
        genericBotCommand.subscribeCommand(discordClient);
    }

    @Test
    public void executeCommand() throws CookieException {
        Mockito.doReturn(true).when(genericBotCommandSpy).parseCommand(Mockito.anyString());
        genericBotCommand.executeCommand(messageCreateEvent);
    }

    @Test
    public void executeCommandFalse() throws CookieException {
        Mockito.doReturn(false).when(genericBotCommandSpy).parseCommand(Mockito.anyString());
        genericBotCommand.executeCommand(messageCreateEvent);
    }

    @Test
    public void executeCommandExecuteThrowsDiscordException() throws CookieException {
        Mockito.when(message.getContent()).thenReturn(Optional.empty());
        genericBotCommand.executeCommand(messageCreateEvent);
    }

    @Test
    public void executeCommandExecuteThrowsCookieException() throws CookieException {
        Mockito.doReturn(true).when(genericBotCommandSpy).parseCommand(Mockito.anyString());
        Mockito.doThrow(new CookieException()).when(genericBotCommandSpy).execute(Mockito.any());
        genericBotCommandSpy.executeCommand(messageCreateEvent);
    }

    @Test
    public void executeCommandExecuteThrowsUnknownException() throws CookieException {
        Mockito.doReturn(true).when(genericBotCommandSpy).parseCommand(Mockito.anyString());
        Mockito.doThrow(new NullPointerException()).when(genericBotCommandSpy).execute(Mockito.any());
        genericBotCommandSpy.executeCommand(messageCreateEvent);
    }

    @Test(expected = InvalidMessageCookieException.class)
    public void parseArgsInvalidMessage() throws CookieException {
        genericBotCommand.parseArgs(null);
    }

    @Test(expected = InvalidMessageCookieException.class)
    public void parseArgsEmptyMessage() throws CookieException {
        genericBotCommand.parseArgs("");
    }
}