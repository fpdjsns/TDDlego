package com.example.study.domain

data class User(val id: String, val password: String? = null) {
    fun matchPassword(password: String?): Boolean {
        return password.equals(this.password)
    }
}