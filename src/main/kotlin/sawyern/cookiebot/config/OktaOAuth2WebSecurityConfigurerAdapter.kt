package sawyern.cookiebot.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class OAuth2ResourceServerSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
                .anyRequest().authenticated()
                .and().oauth2Client()

//        if (http == null)
//            throw SecurityException("HttpSecurity not found")
//
//        http
//                .authorizeRequests()
//                .antMatchers("/test/secure/**", "/api/**")
//                .hasAuthority("Admin")
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Client()
//                .and()
//                .oauth2Login()
//
//        Okta.configureResourceServer401ResponseBody(http);
    }

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/test/**", "/api/**")
    }
}