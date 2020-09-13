package com.sooyeon.sandramall.domain.auth

import com.sooyeon.sandramall.SandraException
import com.sooyeon.sandramall.domain.user.User
import com.sooyeon.sandramall.domain.user.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.sign

@Service
class SignupService @Autowired constructor(private val userRepository: UserRepository) {

    fun signup(signupRequest: SignupRequest) {
        validateRequest(signupRequest)
        checkDuplicatedEmail(signupRequest.email)
        registerUser(signupRequest)
    }

    private fun validateRequest(signupRequest: SignupRequest) {
        validateEmail(signupRequest.email)
        validateName(signupRequest.name)
        validatePassword(signupRequest.password)
    }


    private fun validateEmail(email: String) {

        val isNotValidEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
            .toRegex(RegexOption.IGNORE_CASE)
            .matches(email)
            .not()

        if (isNotValidEmail) {
            throw SandraException("Your email is an invalid email format")
        }
    }

    private fun validateName(name: String) {
        if (name.trim().length !in 2..20) {
            throw SandraException("Your name must be between 8 and 20 characters")
        }
    }

    private fun validatePassword(password: String) {
        if (password.trim().length !in 8..20) {
            throw SandraException("Your password must be between 8 and 20 characters")
        }
    }

    private fun checkDuplicatedEmail(email: String) {
        userRepository.findByEmail(email)?.let {
            throw SandraException("Account already exists with this email")
        }
    }

    private fun registerUser(signupRequest: SignupRequest) =
        with(signupRequest) {
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            val user = User(email, hashedPassword, name)
            userRepository.save(user)
        }

}