package com.sooyeon.sandramall.controller

import com.sooyeon.sandramall.ApiResponse
import com.sooyeon.sandramall.domain.auth.JwtUtil
import com.sooyeon.sandramall.domain.auth.SigninRequest
import com.sooyeon.sandramall.domain.auth.SigninService
import com.sooyeon.sandramall.domain.auth.UserContextHolder
import com.sooyeon.sandramall.interceptor.TokenValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/api/v1")
class SigninApiController @Autowired constructor(
        private val signinService: SigninService,
        private val userContextHolder: UserContextHolder
) {

    @PostMapping("/signin")
    fun signin(@RequestBody signinRequest: SigninRequest) =
            ApiResponse.ok(signinService.signin(signinRequest))

    @PostMapping("/refresh_token")
    fun refreshToken(
            @RequestParam("grant_type") grantType: String
    ): ApiResponse {
        if (grantType != TokenValidationInterceptor.GRANT_TYPE_REFRESH) {
            throw IllegalArgumentException("no grant_type")
        }

        return userContextHolder.email?.let {
            ApiResponse.ok(JwtUtil.createToken(it))
        } ?: throw IllegalArgumentException("no user information")
    }

}