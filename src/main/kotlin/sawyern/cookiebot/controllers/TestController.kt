package sawyern.cookiebot.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.math.log


@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/secure/ping")
    fun hello(principal: Principal): String {
        return "hello ${principal.name}"
    }

    @GetMapping("/nonsecure/ping")
    fun hello2(principal: Principal): String {
        print(SecurityContextHolder.getContext().authentication)
        return "hello ${principal.name}"
    }

    @GetMapping("/ping")
    fun hello(): String {
        return "hello anonymous!"
    }
}