package sawyern.cookiebot.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class OktaOAuth2WebSecurityConfigurerAdapter: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        if (http == null)
            throw SecurityException("HttpSecurity not found")

        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
    }

    override fun configure(web: WebSecurity?) {
        if (web == null)
            throw SecurityException("WebSecurity not found")
        web.ignoring().antMatchers("/test/ping")
    }
}