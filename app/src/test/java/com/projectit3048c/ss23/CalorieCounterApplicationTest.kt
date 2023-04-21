package com.projectit3048c.ss23

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.projectit3048c.MyCalorieCounterApplication
import com.projectit3048c.appModule
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class CalorieCounterApplicationTest {

    private lateinit var application: MyCalorieCounterApplication
    private lateinit var context: Context

    @Before
    fun setUp() {
        application = MyCalorieCounterApplication()
        context = ApplicationProvider.getApplicationContext()
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    /**
     * Tests that [MyCalorieCounterApplication.onCreate] does not throw an exception.
     */
    @Test
    fun `test onCreate`() {
        application.onCreate()
    }

    /**
     * Tests [startKoin] is called with correct parameters
     */
    @Test
    fun `startKoin should be called with correct parameters`() {
        // Given
        mockkStatic(GlobalContext::class)
        every { GlobalContext.getOrNull() } returns null

        // When
        MyCalorieCounterApplication().onCreate()

        // Then
        verify {
            startKoin {
                androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
                androidContext(context)
                modules(appModule)
            }
        }

        unmockkStatic(GlobalContext::class)
    }
}