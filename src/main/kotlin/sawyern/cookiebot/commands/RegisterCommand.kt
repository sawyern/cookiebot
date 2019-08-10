package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.constants.CommandConstants
import sawyern.cookiebot.constants.Constants
import sawyern.cookiebot.services.AccountService
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.CookieService

@Component
class RegisterCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val accountService: AccountService,
        private val cookieService: CookieService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return CommandConstants.CommandName.REGISTER
    }

    override fun getHelpText(): String {
        return "register new account"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val member = botUtilService.getMember(event)

        accountService.registerAccount(member.id.asString(), member.username)
        cookieService.generateCookie(member.id.asString(), Constants.Cookie.STARTING_COOKIES)

        botUtilService.sendMessage(event, "Successfully registered " + member.username)
    }
}