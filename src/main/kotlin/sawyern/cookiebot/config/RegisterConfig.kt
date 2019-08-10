package sawyern.cookiebot.config

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Configuration
@Aspect
class RegisterConfig {
    val logger: Logger = LoggerFactory.getLogger(RegisterConfig::class.java)

    @Before("execution(* sawyern.cookiebot.commands.*.*subscribe(..))")
    fun logRegisterCommand(joinPoint: JoinPoint) {
        logger.info("Registering command: {}", joinPoint.target.javaClass.simpleName)
    }
}