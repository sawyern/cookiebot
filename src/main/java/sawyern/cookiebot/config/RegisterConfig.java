package sawyern.cookiebot.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class RegisterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterConfig.class);

    @Before("execution(* sawyern.cookiebot.bot.*.*subscribeCommand(..))")
    public void logRegisterCommand(JoinPoint joinPoint) {
        LOGGER.info("Registering command: {}", joinPoint.getTarget().getClass().getSimpleName());
    }
}
