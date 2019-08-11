package sawyern.cookiebot.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/secure/ping")
    fun hello(principal: Principal): String {
        return "hello ${principal.name}"
    }

    @GetMapping("/ping")
    fun hello(): String {
        return "hello anonymous!"
    }
}