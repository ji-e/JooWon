package com.example.uohih.joowon.module

import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.ui.main.MainListViewModel
import com.example.uohih.joowon.ui.setting.SettingViewModel
import com.example.uohih.joowon.ui.signin.SignInViewModel
import com.example.uohih.joowon.ui.signup.SignUpViewModel
import com.example.uohih.joowon.ui.vacation.VacationViewModel
import com.example.uohih.joowon.ui.worker.WorkerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { JWBaseViewModel() }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { MainListViewModel(get()) }
    viewModel { VacationViewModel(get()) }
    viewModel { WorkerViewModel(get()) }
}