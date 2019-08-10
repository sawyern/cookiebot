package sawyern.cookiebot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.models.Account
import sawyern.cookiebot.repository.AccountRepository

@Service
class AccountService @Autowired constructor(private val accountRepository: AccountRepository) {

    private val logger: Logger = LoggerFactory.getLogger(AccountService::class.java)
    
    fun registerAccount(discordId: String, username: String): Account {
        if (accountRepository.findByDiscordId(discordId) != null) {
            throw CookieException("Account already registered")
        }

        var account = Account(discordId, username)
        account = accountRepository.save(account)
        logger.info("Registered new account: $username")
        return account
    }

    fun getAccount(discordId: String): Account {
        return accountRepository.findByDiscordId(discordId) ?: throw CookieException("Unable to find account with id $discordId")
    }

    fun deleteAccount(discordId: String) {
        val account = accountRepository.findByDiscordId(discordId) ?: throw CookieException("Account does not exist therefore cannot be deleted")
        accountRepository.delete(account)
        logger.info("Account deleted: ${account.username}")
    }

    fun findAll(): List<Account> {
        return accountRepository.findAll()
    }
}