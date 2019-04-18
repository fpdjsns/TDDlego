package com.example.study

import com.example.study.domain.User
import com.example.study.domain.UserRepository
import com.example.study.security.AuthService
import com.example.study.security.NonExistingUserException
import com.example.study.security.WrongPasswordException
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AuthServiceTest {
    val USER_PASSWORD = "userPassword"
    val mockUserRepository: UserRepository = mock(UserRepository::class.java)
    val authService: AuthService = AuthService(mockUserRepository)

    @Before
    fun setUp() {
    }

    val USER_ID = "userId"
    val WRONG_PASSWORD = "wrongPassword"

    @Test
    fun givenInvalidId_throwIllegalArgEx() {
        assertIllegalArgExThrown(null, USER_PASSWORD)
        assertIllegalArgExThrown("", USER_PASSWORD)
        assertIllegalArgExThrown(USER_ID, null)
        assertIllegalArgExThrown(USER_ID, "")
    }

    @Test
    fun whenUserNotFound_throwNonExistingUserEx(){
        assertExceptionThrown("noUserId", USER_PASSWORD, NonExistingUserException::class.java)
        assertExceptionThrown("noUserId2", USER_PASSWORD, NonExistingUserException::class.java)
    }

    @Test
    fun whenUserFoundButWrongPw_throwWrongPasswordEx() {
        givenUserExists(USER_ID, USER_PASSWORD)
        assertExceptionThrown(USER_ID, WRONG_PASSWORD, WrongPasswordException::class.java)
        verifyUserFound(USER_ID)
    }

    @Test
    fun whenUserFoundAndRightPw_returnAuth(){
        givenUserExists(USER_ID, USER_PASSWORD)
        val auth = authService.authenticate(USER_ID,USER_PASSWORD)
        assertThat(auth.id, equalTo(USER_ID))
    }

    private fun givenUserExists(id: String, password: String) {
        `when`(mockUserRepository.findById(id)).thenReturn(User(id, password))
    }

    private fun verifyUserFound(id: String) {
        verify(mockUserRepository).findById(id)
    }

    private fun assertIllegalArgExThrown(id: String?, password: String?) {
        assertExceptionThrown(id, password, IllegalArgumentException::class.java)
    }

    private fun assertExceptionThrown(id: String?, password: String?, type: Class<out Exception>) {
        var thrownEx: Exception? = null
        try {
            authService!!.authenticate(id, password)
        } catch (e: Exception) {
            thrownEx = e
        }
        assertThat(thrownEx, instanceOf(type))
    }


}

