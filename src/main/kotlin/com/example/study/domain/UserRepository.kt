package com.example.study.domain

class UserRepository {
    fun findById(id: String): User {
        return User(id)
    }

}