package com.example.uohih.joowon.module


import com.example.uohih.joowon.data.main.MainListRepository
import com.example.uohih.joowon.data.main.MainListRepositoryImpl
import com.example.uohih.joowon.data.signin.SignInRepository
import com.example.uohih.joowon.data.signin.SignInRepositoryImpl
import com.example.uohih.joowon.data.setting.SettingRepository
import com.example.uohih.joowon.data.setting.SettingRepositoryImpl
import com.example.uohih.joowon.data.signup.SignUpRepository
import com.example.uohih.joowon.data.signup.SignUpRepositoryImpl
import com.example.uohih.joowon.data.vacation.VacationRepository
import com.example.uohih.joowon.data.vacation.VacationRepositoryImpl
import com.example.uohih.joowon.data.worker.WorkerRepository
import com.example.uohih.joowon.data.worker.WorkerRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SignInRepository> { SignInRepositoryImpl(get()) }
    single<SignUpRepository> { SignUpRepositoryImpl(get()) }
    single<SettingRepository> { SettingRepositoryImpl(get()) }
    single<MainListRepository> { MainListRepositoryImpl(get()) }
    single<VacationRepository> { VacationRepositoryImpl(get()) }
    single<WorkerRepository> { WorkerRepositoryImpl(get()) }
}

