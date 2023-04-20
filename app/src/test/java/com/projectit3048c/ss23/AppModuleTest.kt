package com.projectit3048c

import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest
import org.koin.test.inject

class AppModuleTest : KoinTest {

    private val testModule = module {
        single<IFoodService> { mockk<FoodService>() }
    }

    @Test
    fun `test_Koin`() {
        val appModules = listOf(testModule, appModule)
        startKoin {
            modules(appModules)
        }

        val foodService: IFoodService by inject()
        assertNotNull(foodService)

        stopKoin()
    }
}