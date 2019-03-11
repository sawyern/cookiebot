package sawyern.cookiebot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.LootboxToken;
import sawyern.cookiebot.repository.LootboxTokenRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
public class LootboxTokenService {

    private AccountService accountService;
    private LootboxTokenRepository lootboxTokenRepository;

    private static Logger LOGGER = LoggerFactory.getLogger(LootboxTokenService.class);

    public List<LootboxToken> getAllByAccount(String discordId) throws CookieException {
        try {
            return lootboxTokenRepository.findByAccount(accountService.getAccount(discordId));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Error getting all tokens by account.");
        }
    }

    public void deleteLootBoxToken(String discordId) throws CookieException {
        try {
            List<LootboxToken> tokens = getAllByAccount(discordId);
            if (!tokens.isEmpty()) {
                LootboxToken token = tokens.stream().findFirst().orElseThrow(() -> new CookieException("Error getting tokens."));
                lootboxTokenRepository.delete(token);
            }
            LOGGER.warn("No token deleted");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Error deleting tokens.");
        }
    }

    public void addLootboxToken(String discordId, int num) throws CookieException {
        try {
            Account account = accountService.getAccount(discordId);
            for (int i = 0; i < num; i++) {
                LootboxToken token = new LootboxToken();
                token.setAccount(account);
                lootboxTokenRepository.save(token);
            }
            LOGGER.info(MessageFormat.format("Created {0} tokens for account {1}", num, discordId));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Error adding token.");
        }
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setLootboxTokenRepository(LootboxTokenRepository lootboxTokenRepository) {
        this.lootboxTokenRepository = lootboxTokenRepository;
    }
}
