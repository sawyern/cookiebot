package sawyern.cookiebot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.models.dto.AccountDto;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.repository.AccountRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class AccountService {

    private AccountRepository accountRepository;

    private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * creates new account if not already exists
     * @param accountDto
     * @throws CookieException
     */
    @Transactional
    public void registerAccount(AccountDto accountDto) throws CookieException {
        if (accountRepository.findByDiscordId(accountDto.getDiscordId()).isPresent())
            throw new CookieException("Account already registered.");

        Account account = new Account(accountDto.getDiscordId(), accountDto.getUsername());
        try {
            accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Failed to create account");
        }
    }

    /**
     * gets account by id
     * @param discordId
     * @return
     * @throws CookieException
     */
    public Account getAccount(String discordId) throws CookieException {
        return accountRepository.findByDiscordId(discordId).orElseThrow(() -> new CookieException("Unable to find account with id " + discordId, HttpStatus.NOT_FOUND));
    }

    /**
     * deletes account if exists
     * @param discordId
     * @throws CookieException
     */
    public void deleteAccount(String discordId) throws CookieException {
        Account account = accountRepository.findByDiscordId(discordId).orElse(null);
        if (account == null)
            return;
        try {
            accountRepository.delete(account);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Failed to delete account");
        }
    }

    public List<Account> findAllAccount() throws CookieException {
        try {
            return accountRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Failed to find all accounts.");
        }
    }
}
