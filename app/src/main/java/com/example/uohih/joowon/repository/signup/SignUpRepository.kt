package com.example.uohih.joowon.repository.signup

import com.example.uohih.joowon.model.Result
import com.example.uohih.joowon.model.signup.SignUpDataSource
import org.json.JSONObject

class SignUpRepository(val dataSource: SignUpDataSource) {
    fun signUp(email: String, password: String): Result<JSONObject> {
        // handle login
        val result = dataSource.signUp(email, password)

        if (result is Result.Success) {
//            setLoggedInUser(result.data)
        }

        return result
    }

}

