package com.example.study.security

import com.example.study.domain.Authentication
import com.example.study.domain.User
import com.example.study.domain.UserRepository

class AuthService(var userRepository: UserRepository) {

    fun authenticate(id: String?, password: String?) : Authentication {
        assertIdAndPw(id, password)
        val user: User = findUserOrThrowEx(id)
        throwExIfPwWrong(user, password)
        return createAuthentication(user)
    }

    private fun assertIdAndPw(id: String?, password: String?) {
        if (id.isNullOrEmpty()) {
            throw IllegalArgumentException()
        }
        if (password.isNullOrEmpty()) {
            throw IllegalArgumentException()
        }
    }

    private fun findUserOrThrowEx(id: String?): User {
        val user: User = id?.let{findUserById(id)}
                ?: throw NonExistingUserException()
        return user
    }

    private fun throwExIfPwWrong(user: User, password: String?) {
        if (!user.matchPassword(password))
            throw WrongPasswordException()
    }

    private fun createAuthentication(user: User) = Authentication(user.id)

    private fun findUserById(id: String): User? {
        return userRepository.findById(id)
//            if(id.equals("userId"))
//                return User(id, "1234")
//            return null
    }
}