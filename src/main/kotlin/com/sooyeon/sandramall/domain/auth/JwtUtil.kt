package com.sooyeon.sandramall.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

object JwtUtil {

    private const val ISSUER = "SandraMall"
    private const val SUBJECT = "Auth"
    private const val EXPIRE_TIME = 60L * 60 * 2 * 1000 // 2 hours
    private const val REFRESH_EXPIRE_TIME = 60L * 60 * 24 * 30 * 1000 // 30 days

    private val SECRET = "sandramallsecret"
    private val algorithm: Algorithm = Algorithm.HMAC256(SECRET)

    private val refreshSecret = "sandramallrefreshsecret"
    private val refreshAlgorithm: Algorithm = Algorithm.HMAC256(refreshSecret)

    fun createToken(email: String) = JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + EXPIRE_TIME))
            .withClaim(JwtClaims.EMAIL, email)
            .sign(algorithm)

    fun createRefreshToken(email: String) = JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + REFRESH_EXPIRE_TIME))
            .withClaim(JwtClaims.EMAIL, email)
            .sign(refreshAlgorithm)

    fun verify(token: String): DecodedJWT =
            JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)

    fun verifyRefresh(token: String): DecodedJWT =
            JWT.require(refreshAlgorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)

    fun extractEmail(jwt: DecodedJWT): String =
            jwt.getClaim(JwtClaims.EMAIL).asString()

    object JwtClaims {
        const val EMAIL = "email"
    }
}