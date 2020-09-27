package com.sooyeon.sandramall.interceptor

import com.sooyeon.sandramall.domain.auth.JwtUtil
import com.sooyeon.sandramall.domain.auth.UserContextHolder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenValidationInterceptor @Autowired constructor(
        private val userContextHolder: UserContextHolder
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun preHandle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any): Boolean {
        val authHeader = request.getHeader(AUTHORIZATION)

        if (authHeader.isNullOrBlank()) {
            val pair = request.method to request.servletPath
            if (!DEFAULT_ALLOWED_API_URLS.contains(pair)) {
                response.sendError(401)
                return false
            }
            return true
        } else {
            val grantType = request.getParameter("grant_type")
            val token = extractToken(authHeader)
            return handleToken(grantType, token, response)
        }

    }

    private fun handleToken(
            grantType: String,
            token: String,
            response: HttpServletResponse) =
            try {
                val jwt = when (grantType) {
                    GRANT_TYPE_REFRESH -> JwtUtil.verifyRefresh(token)
                    else -> JwtUtil.verify(token)
                }
                val email = JwtUtil.extractEmail(jwt)
                userContextHolder.set(email)
                true
            } catch (e: Exception) {
                logger.error("failed to validate token $token", e)
                response.sendError(401)
                false
            }

    private fun extractToken(token: String) =
            token.replace(BEARER, "").trim()

    override fun postHandle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any,
            modelAndView: ModelAndView?) {
        userContextHolder.clear()
    }

    companion object {

        private const val AUTHORIZATION = "authorization"
        private const val BEARER = "bearer"
        private const val GRANT_TYPE = "grant_type"
        const val GRANT_TYPE_REFRESH = "refresh_token"

        private val DEFAULT_ALLOWED_API_URLS = listOf(
                "POST" to "/api/v1/signin",
                "POST" to "/api/v1/users"
        )
    }
}
