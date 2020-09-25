package com.example.uohih.joowon.ui.signin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.model.signin.LoginDataSource
import com.example.uohih.joowon.repository.signin.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 *
 * viewmodel이 UI에 표시할 데이터를 보여주기 위해 필요로 하는여러 인스턴스들을 넘겨줘야한다(repository나 usecase 같은 것들)
 * 그래서 안드로이드는 Factory라는 클래스를 통해서
 * viewmodel에 원하는 인자를 넣어 생성할수 있도록 하는 방법을 제공해준다
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginRepository = LoginRepository(
                            dataSource = LoginDataSource()
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
