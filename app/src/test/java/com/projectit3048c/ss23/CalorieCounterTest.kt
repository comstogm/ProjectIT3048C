package com.projectit3048c.ss23

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.projectit3048c.MainViewModel
import com.projectit3048c.dto.Food
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TestRule
import com.projectit3048c.service.FoodService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CalorieCounterTest {

    lateinit var mvm: MainViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var foodService: FoodService
    var allFoods: List<Food>? = ArrayList<Food>()

    //lateinit var mvm: MainViewModel

    @MockK
    lateinit var mockFoodService: FoodService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Giving food calorie, data available when I search for egg then I should receive 1 egg`() =
        runTest {
            givingFoodServiceIsInitialized()
            whenFoodDataAreReadAndParsed()
            thenTheFoodCollectionShouldContainEgg()
        }

    private fun givingFoodServiceIsInitialized() {
        foodService = FoodService()
    }

    private suspend fun whenFoodDataAreReadAndParsed() {
        allFoods = foodService.fetchFoods()
    }

    private fun thenTheFoodCollectionShouldContainEgg() {
        assertNotNull(allFoods)
        assertTrue(allFoods!!.isNotEmpty())
        var containsEgg = false

        allFoods!!.forEach {
            if (it.name.contains("Egg")) {
                containsEgg = true
            }
        }
        assertTrue(containsEgg)
    }

    @Test
    fun confirmEgg_output() {
        val food: Food = Food(3, "Egg", "1 egg", 155)
        assertEquals("Egg", food.toString())
    }


    @Test
    fun `giving a view model with live data when populated with foods then result show Apple`() {
        givenViewModelIsInitializesWithMockData()
        whenFoodServiceFetchFoodsInvoked()
        thenResultShouldContainApple()
    }

    private fun givenViewModelIsInitializesWithMockData() {
        val foods = ArrayList<Food>()
        foods.add(Food(1, "Apple", "1 apple(182g)", 52))
        foods.add(Food(3, "Egg", "1 egg", 155))
        foods.add(Food(2, "Milk", "100g 1% fat",42))

        coEvery { mockFoodService.fetchFoods() } returns foods

        //mvm = MainViewModel(foodService = mockFoodService)
        mvm = MainViewModel(foodService = mockFoodService)
        //mvm.foodService = mockFoodService
    }

    private fun whenFoodServiceFetchFoodsInvoked() {
        mvm.fetchFoods()
    }

    private fun thenResultShouldContainApple() {
        var allFoods: List<Food>? = ArrayList<Food>()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<Food>?> {
            override fun onChanged(receivedFoods: List<Food>?) {
                allFoods = receivedFoods
                latch.countDown()
                mvm.foods.removeObserver(this)
            }
        }
        mvm.foods.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allFoods)

        assertTrue(allFoods!!.isNotEmpty())
        var containsEgg = false
        allFoods!!.forEach {
            if (it.name.contains("Apple")) {
                containsEgg = true
            }
        }
        assertTrue(containsEgg)
    }
}




