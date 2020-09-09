package com.sooyeon.sandramall

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloApiController{

    @GetMapping("/api/v1/hello")
    fun hello() = ApiResponse.ok("world")
}