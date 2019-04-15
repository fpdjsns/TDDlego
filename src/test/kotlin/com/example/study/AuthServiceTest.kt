package com.example.study

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

    @Test
    fun givenInvalidId_throwIllegalArgEx() {
        assertIllegalArgExThrown(null, USER_PASSWORD)
        assertIllegalArgExThrown("", USER_PASSWORD)
        assertIllegalArgExThrown("userId", null)
        assertIllegalArgExThrown("userId", "")
    }

    @Test
    fun whenUserNotFound_throwNonExistingUserEx(){
        assertExceptionThrown("noUserId", USER_PASSWORD, NonExistingUserException::class.java)
        assertExceptionThrown("noUserId2", USER_PASSWORD, NonExistingUserException::class.java)
    }

    @Test
    fun whenUserFoundButWrongPw_throwWrongPasswordEx() {
        givenUserExists("userId", USER_PASSWORD)
        assertExceptionThrown("userId", "wrongPassword", WrongPasswordException::class.java)
        verifyUserFound("userId")
    }

    private fun givenUserExists(id: String, password: String) {
        `when`(mockUserRepository.findById(id)).thenReturn(User(id, password))
    }

    private fun verifyUserFound(id: String) {
        verify(mockUserRepository).findById(id)
    }

    class UserRepository {
        fun findById(id: String): User {
            return User(id)
        }

    }

    class WrongPasswordException : Exception(){

    }

    class NonExistingUserException : Exception(){

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


    class AuthService(var userRepository: UserRepository) {

        fun authenticate(id: String?, password: String?) {
            if(id.isNullOrEmpty()){
                throw IllegalArgumentException()
            }
            if(password.isNullOrEmpty()){
                throw IllegalArgumentException()
            }

            val user: User = findUserById(id)
                    ?: throw NonExistingUserException()
            throw WrongPasswordException()
        }

        private fun findUserById(id: String): User? {
            return userRepository.findById(id)
//            if(id.equals("userId"))
//                return User(id, "1234")
//            return null
        }
    }

    data class User(val id: String, val password: String? = null)
}