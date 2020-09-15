package com.sooyeon.sandramall.domain.auth

import com.sooyeon.sandramall.SandraException
import com.sooyeon.sandramall.domain.user.User
import com.sooyeon.sandramall.domain.user.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalStateException

@Service
class SigninService @Autowired constructor(
    private val userRepository: UserRepository
) {

    fun signin(signinRequest: SigninRequest): SigninResponse {
        val user = userRepository
            .findByEmail(signinRequest.email.toLowerCase())
            ?: throw SandraException("unregistered email")

        if (isNotValidPassword(signinRequest.password, user.password)) {
            throw SandraException("incorrect password")
        }

        return responseWithTokens(user)
    }

    private fun isNotValidPassword(plain: String, hashed: String) = BCrypt.checkpw(plain, hashed).not()

    private fun responseWithTokens(user: User) = user.id?.let { userId ->
        SigninResponse(
            JwtUtil.createToken(user.email),
            JwtUtil.createRefreshToken(user.email),
            user.name,
            userId
        )
    } ?: throw IllegalStateException("no user ID")

}