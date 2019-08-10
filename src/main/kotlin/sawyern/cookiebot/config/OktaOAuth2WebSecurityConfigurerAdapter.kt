package sawyern.cookiebot.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class OktaOAuth2WebSecurityConfigurerAdapter: WebSecurityConfigurerAdapter() {
    override fun configure(web: WebSecurity?) {
        http
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt();
    }
}