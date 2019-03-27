package sawyern.cookiebot.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
@Slf4j
public class RegisterConfig {
    @Before("execution(* sawyern.cookiebot.bot.*.*subscribeCommand(..))")
    public void logRegisterCommand(JoinPoint joinPoint) {
        log.info("Registering command: {}", joinPoint.getTarget().getClass().getSimpleName());
    }
}
