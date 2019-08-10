package sawyern.cookiebot.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/cookiebot/v1")
class TestController {
    @GetMapping("/test")
    fun hello(principal: Principal): String {
        return "hello ${principal.name}"
    }
}