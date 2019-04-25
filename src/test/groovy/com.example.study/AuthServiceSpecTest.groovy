package com.example.study

import com.example.study.domain.User
import com.example.study.domain.UserRepository
import com.example.study.security.AuthService
import com.example.study.security.NonExistingUserException
import com.example.study.security.WrongPasswordException
import spock.lang.*

class AuthServiceSpecTest extends Specification{

    def mockUserRepository = GroovyMock(UserRepository.class)
    def authService = new AuthService(mockUserRepository)

    static def USER_PASSWORD = "userPassword"
    static def USER_ID = "userId"
    static def WRONG_PASSWORD = "wrongPassword"

//
//    def whenUserFoundButWrongPw_throwWrongPasswordEx() {
//        givenUserExists(USER_ID, USER_PASSWORD)
//        assertExceptionThrown(USER_ID, WRONG_PASSWORD, WrongPasswordException.class)
//        verifyUserFound(USER_ID)
//    }
//
//    def whenUserFoundAndRightPw_returnAuth(){
//        givenUserExists(USER_ID, USER_PASSWORD)
//        val auth = authService.authenticate(USER_ID,USER_PASSWORD)
//        assertThat(auth.id, equalTo(USER_ID))
//    }
//
//    private def givenUserExists(String id, String password) {
//        mockUserRepository.findById(id) >> User(id, password)
//    }
//
//    private def verifyUserFound(String id) {
//        verify(mockUserRepository).findById(id)
//    }

    def "assertExceptionThrown"() {
        given:
        mockUserRepository.findById(id) >> user

        expect:
        Exception thrownEx = null

        try {
            authService.authenticate(id, password)
        } catch (Exception e) {
            thrownEx = e
        }

        thrownEx.class == type

        where:
        id      | password      | user || type

        // givenInvalidId_throwIllegalArgEx
        null    | USER_PASSWORD | null                       || IllegalArgumentException.class
        ""      | USER_PASSWORD | null                       || IllegalArgumentException.class
        USER_ID | null          | null                       || IllegalArgumentException.class
        USER_ID | ""            | null                       || IllegalArgumentException.class
//
//        // whenUserNotFound_throwNonExistingUserEx()
//        "noUserId" | USER_PASSWORD | null                    || NonExistingUserException.class
//        "noUserId2" | USER_PASSWORD | null                   || NonExistingUserException.class
//
//        // whenUserFoundButWrongPw_throwWrongPasswordEx()
//        USER_ID | WRONG_PASSWORD | new User(id, password) || WrongPasswordException.class
    }
}