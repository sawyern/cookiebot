package sawyern.cookiebot.controllers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.oauth2.core.oidc.user.OidcUser


@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/secure/ping")
    fun hello(@AuthenticationPrincipal user: OidcUser): String {
        return "hello ${user.name}"
    }

    @GetMapping("/nonsecure/ping")
    fun hello2(@AuthenticationPrincipal user: OidcUser): String {
        return "hello ${user.name}"
    }

    @GetMapping("/ping")
    fun hello(): String {
        return "hello anonymous!"
    }
}