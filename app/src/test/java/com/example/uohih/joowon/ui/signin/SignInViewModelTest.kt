package com.example.uohih.joowon.ui.signin

import com.example.uohih.joowon.AbstrackKoinTest
import com.example.uohih.joowon.data.signin.SignInRepository
import com.example.uohih.joowon.data.signin.SignInRepositoryImpl
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given

class SignInViewModelTest : AbstrackKoinTest() {

    val signInViewModel: SignInViewModel by inject()

    //    val signInRepository = declareMock<SignInRepository> {
//        given()
//    }
    lateinit var signInRepository: SignInRepository

    @Before
    fun setUp() {
        signInRepository = SignInRepositoryImpl()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getJw1001Data() {
    }

    @Test
    fun getJw1002Data() {
    }

    @Test
    fun getJw1003Data() {
    }

    @Test
    fun getJw1005Data() {
    }

    @Test
    fun getJw1006Data() {
    }

    @Test
    fun getJw2001Data() {
    }

    @Test
    fun getSignInFormState() {
    }

    @Test
    fun isEmailValid() {
    }

    @Test
    fun isPasswordValid() {
    }

    @Test
    fun isEmailOverlapConfirm() {
    }

    @Test
    fun signUp() {
    }

    @Test
    fun getSnsSignInInfo() {
    }

    @Test
    fun updateAdminInfo() {
    }

    @Test
    fun getAdminInfo() {
    }

    @Test
    fun signIn() {
    }
}