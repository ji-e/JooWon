package com.example.uohih.joowon.model.signup

import com.example.uohih.joowon.model.Result
import org.json.JSONObject
import java.io.IOException

class SignUpDataSource {
    fun signUp(email: String, password: String): Result<JSONObject> {
        try {
            // TODO: handle loggedInUser authentication
//            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            val jsonObject = JSONObject()
            return Result.Success(jsonObject)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}