package com.example.uohih.joowon.module

import com.example.uohih.joowon.data.main.MainListDataSource
import com.example.uohih.joowon.data.main.MainListDataSourceImpl
import com.example.uohih.joowon.data.setting.SettingDataSource
import com.example.uohih.joowon.data.setting.SettingDataSourceImpl
import com.example.uohih.joowon.data.signin.SignInDataSource
import com.example.uohih.joowon.data.signin.SignInDataSourceImpl
import com.example.uohih.joowon.data.signup.SignUpDataSource
import com.example.uohih.joowon.data.signup.SignUpDataSourceImpl
import com.example.uohih.joowon.data.vacation.VacationDataSource
import com.example.uohih.joowon.data.vacation.VacationDataSourceImpl
import com.example.uohih.joowon.data.worker.WorkerDataSourceImpl
import com.example.uohih.joowon.data.worker.WorkerDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<SignInDataSource> { SignInDataSourceImpl(get()) }
    single<SignUpDataSource> { SignUpDataSourceImpl(get()) }
    single<SettingDataSource> { SettingDataSourceImpl(get()) }
    single<MainListDataSource> { MainListDataSourceImpl(get()) }
    single<VacationDataSource> { VacationDataSourceImpl(get()) }
    single<WorkerDataSource> { WorkerDataSourceImpl(get()) }
}