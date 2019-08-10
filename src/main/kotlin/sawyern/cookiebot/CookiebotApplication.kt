package sawyern.cookiebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["sawyern.cookiebot"])
@EntityScan
@EnableConfigurationProperties
@EnableJpaRepositories
class CookiebotApplication
fun main(args: Array<String>) {
	runApplication<CookiebotApplication>(*args)
}
