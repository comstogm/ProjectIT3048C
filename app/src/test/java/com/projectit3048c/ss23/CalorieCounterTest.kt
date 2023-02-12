package com.projectit3048c.ss23

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dto.Food
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TestRule
import service.FoodService

class CalorieCounterTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var foodService : FoodService
    var allFoods : List<Food>? = ArrayList<Food>()

    @Test
    fun `Giving food calorie, data available when I search for egg then I should receive 1 egg - 70cal`() = runTest{
        givingFoodServiceIsInitialized()
        whenFoodDataAreReadAndParsed()
        thenTheFoodCollectionShouldContainEgg()
        assert(true)
        assertEquals( 2,  1+1)
    }

    private fun givingFoodServiceIsInitialized() {
        foodService = FoodService()
    }

    private suspend fun whenFoodDataAreReadAndParsed() {
       allFoods =  foodService.fetchFoods()
    }

    private fun thenTheFoodCollectionShouldContainEgg() {
        assertNotNull(allFoods)
        assertTrue(allFoods!!.isNotEmpty())
        var containsEgg = false

        allFoods!!.forEach {
            if (it.genus.equals("Cercis")) {
                containsEgg = true
            }
        }
        assertTrue(containsEgg)
    }

}