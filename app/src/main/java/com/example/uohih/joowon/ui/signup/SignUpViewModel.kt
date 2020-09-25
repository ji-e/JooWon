package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.R
import com.example.uohih.joowon.model.Result
import com.example.uohih.joowon.model.signup.SignUpFormState
import com.example.uohih.joowon.model.signup.SignUpResult
import com.example.uohih.joowon.repository.signup.SignUpRepository

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    private val _signResult = MutableLiveData<SignUpResult>()

    val signUpFormState: LiveData<SignUpFormState> = _signUpForm
    val signResult: LiveData<SignUpResult> = _signResult

    fun signUp(email: String, password: String) {
        val result = signUpRepository.signUp(email, password)

        if (result is Result.Success) {
            _signResult.value = SignUpResult(success = R.string.login_failed)
        } else {
            _signResult.value = SignUpResult(error = R.string.login_failed)
        }
    }

    val testTV = "fdfdf"
}