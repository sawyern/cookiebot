package sawyern.cookiebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = "sawyern.cookiebot")
@EntityScan
@EnableJpaRepositories
@EnableSwagger2
public class CookieBotApplication {
	public static void main(String[] args) {
		SpringApplication.run(CookieBotApplication.class, args);
	}
}
