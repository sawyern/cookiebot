package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
public class CookiesCommandTest {

    @InjectMocks
    private CookiesCommand cookiesCommand;

    @Mock
    private CookieService cookieService;
    @Mock
    private MessageCreateEvent event;

    @Test
    public void getCommand() {
        Assert.assertEquals("cookies", cookiesCommand.getCommand());
    }

    @Test
    public void getAllowedNumArgs() {
        Assert.assertTrue(cookiesCommand.getAllowedNumArgs().contains(0));
        Assert.assertTrue(cookiesCommand.getAllowedNumArgs().contains(1));
        Assert.assertEquals(2, cookiesCommand.getAllowedNumArgs().size());
    }

    @Test(expected = CookieException.class)
    public void executeGiven0ArgsAndErrorGettingMemberThenThrowsCookieException() throws CookieException {
        Mockito.when(event.getMember()).thenReturn(Optional.ofNullable(null));
        cookiesCommand.execute(event, new ArrayList<>());
    }
}
