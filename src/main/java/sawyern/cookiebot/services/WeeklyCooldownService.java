package sawyern.cookiebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.WeeklyCooldown;
import sawyern.cookiebot.repository.WeeklyCooldownRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class WeeklyCooldownService {
    private WeeklyCooldownRepository weeklyCooldownRepository;
    private AccountService accountService;

    private static final long WEEKLY_COOLDOWN = 1L;

    public boolean isCooldown(String discordId) throws CookieException {
        WeeklyCooldown cooldown = getCooldownByAccountId(discordId);
        return LocalDateTime.now().isAfter(cooldown.getCanUseNext());
    }

    public void putOnCooldown(String discordId) throws CookieException {
        try {
            WeeklyCooldown cooldown = getCooldownByAccountId(discordId);
            if (isCooldown(discordId)) {
                cooldown.setCanUseNext(LocalDateTime.now().plusWeeks(WEEKLY_COOLDOWN));
                weeklyCooldownRepository.save(cooldown);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Error putting weekly cooldown.");
        }
    }

    public WeeklyCooldown getCooldownByAccountId(String discordId) throws CookieException {
        try {
            WeeklyCooldown cooldown = weeklyCooldownRepository.findByAccount(accountService.getAccount(discordId));
            if (cooldown == null)
                cooldown = createCooldown(discordId);
            return cooldown;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Error getting cooldown by account");
        }
    }

    public WeeklyCooldown createCooldown(String discordId) throws CookieException {
        Account account = accountService.getAccount(discordId);
        WeeklyCooldown cooldown = weeklyCooldownRepository.findByAccount(account);
        if (cooldown == null) {
            cooldown = new WeeklyCooldown();
            cooldown.setAccount(account);
            cooldown.setCanUseNext(LocalDateTime.now().minus(Duration.ofSeconds(1)));
            weeklyCooldownRepository.save(cooldown);
            log.info("New cooldown created.");
        }
        return cooldown;
    }

    public long getRemainingCooldownHours(String discordId) throws CookieException {
        WeeklyCooldown cooldown = getCooldownByAccountId(discordId);
        return Duration.between(LocalDateTime.now(), cooldown.getCanUseNext()).toHours();
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setWeeklyCooldownRepository(WeeklyCooldownRepository weeklyCooldownRepository) {
        this.weeklyCooldownRepository = weeklyCooldownRepository;
    }
}
