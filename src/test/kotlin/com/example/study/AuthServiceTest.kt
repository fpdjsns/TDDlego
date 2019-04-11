package com.example.study

import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class AuthServiceTest {
    var authService: AuthService? = null
    val USER_PASSWORD = "userPassword"

    @Before
    fun canCreate() {
        authService = AuthService()
    }

    @Test
    fun givenInvalidId_throwIllegalArgEx() {
        assertIllegalArgExThrown(null, USER_PASSWORD)
        assertIllegalArgExThrown("", USER_PASSWORD)
        assertIllegalArgExThrown("userId", null)
        assertIllegalArgExThrown("userId", "")
    }

    private fun assertIllegalArgExThrown(id: String?, password: String?) {
        var thrownEx: Exception? = null
        try {
            authService!!.authenticate(id, password)
        } catch (e: Exception) {
            thrownEx = e
        }
        assertThat(thrownEx, instanceOf(IllegalArgumentException::class.java))
    }


    class AuthService {
        fun authenticate(id: String?, password: String?) {
            if(id.isNullOrEmpty()){
                throw IllegalArgumentException()
            }
            if(password.isNullOrEmpty()){
                throw IllegalArgumentException()
            }
        }
    }
}