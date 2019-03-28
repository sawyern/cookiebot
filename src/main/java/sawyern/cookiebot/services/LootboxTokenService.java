package sawyern.cookiebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.LootboxToken;
import sawyern.cookiebot.repository.LootboxTokenRepository;

import java.util.List;

@Service
@Slf4j
public class LootboxTokenService {

    private AccountService accountService;
    private LootboxTokenRepository lootboxTokenRepository;

    public List<LootboxToken> getAllByAccount(String discordId) throws CookieException {
        try {
            return lootboxTokenRepository.findByAccountDiscordId(discordId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
            else log.warn("No token deleted");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
            log.info("Created {} tokens for account {}", num, discordId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
