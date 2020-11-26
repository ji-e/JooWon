package com.example.uohih.joowon

import com.example.uohih.joowon.module.apiModule
import com.example.uohih.joowon.module.dataSourceModule
import com.example.uohih.joowon.module.repositoryModule
import com.example.uohih.joowon.module.viewModelModule
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

abstract class AbstrackKoinTest : KoinTest {

//    @Before
//    fun start() {
//        MockitoAnnotations.initMocks(this)
//        startKoin {
//            modules(listOf(
//                    apiModule,
//                    repositoryModule,
//                    dataSourceModule,
//                    viewModelModule))
//        }
//
//    }


    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(listOf(
                apiModule,
                repositoryModule,
                dataSourceModule,
                viewModelModule))
    }

    @get:Rule
    val mockProvider = MockProviderRule.create {
        Mockito.mock(it.java)
    }
}