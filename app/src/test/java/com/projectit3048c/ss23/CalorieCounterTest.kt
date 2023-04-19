package com.projectit3048c.ss23

import com.projectit3048c.ss23.ui.theme.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TestRule
import com.projectit3048c.service.FoodService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import java.util.*
import kotlin.collections.ArrayList
import com.projectit3048c.dto.*

class CalorieCounterTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var foodService: FoodService
    var allFoods: List<Food>? = ArrayList<Food>()

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
    fun `Given food data is available, when I search for egg, then I should receive 1 egg`() =
        runTest {
            givenFoodServiceIsInitialized()
            whenFoodDataAreReadAndParsed()
            thenTheFoodCollectionShouldContainEgg()
        }

    private fun givenFoodServiceIsInitialized() {
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
    fun `Given a food with a name, when calling toString(), then return that food's name`() {
        val food: Food = Food(3, "Egg", "1 egg", "155")
        assertEquals("Egg", food.toString())
    }

    @Test
    fun `Given calories, when setting and getting the value, the value is returned correctly`() {
        val calories = Calories()
        calories.calories = "100"
        assertEquals("100", calories.calories)
    }

    @Test
    fun `Given a food amount, when toString is called, then return food name`() {
        val foodAmount = FoodAmount(foodId = "123", foodName = "Apple", internalFoodID = 1, foodIntake = "Breakfast", foodAmount = "1", foodDate = "2022-04-18")
        val result = foodAmount.toString()
        assertEquals("Apple", result)
    }

    @Test
    fun `When generating a photo object, then properties are initialized correctly`() {
        val localUri = "localUri"
        val remoteUri = "remoteUri"
        val description = "description"
        val dateTaken = Date()
        val id = "id"

        val photo = Photo(localUri, remoteUri, description, dateTaken, id)

        assertEquals(localUri, photo.localUri)
        assertEquals(remoteUri, photo.remoteUri)
        assertEquals(description, photo.description)
        assertEquals(dateTaken, photo.dateTaken)
        assertEquals(id, photo.id)
    }

    @Test
    fun `When generating a new User with a uid and displayName, then uid and displayName should be set`() {
        val uid = "abc123"
        val displayName = "Jim Barnett"

        val user = User(uid, displayName)

        assertEquals(uid, user.uid)
        assertEquals(displayName, user.displayName)
    }

    @Test
    fun testColors() {
        assertEquals(Color(0xFFBB86FC), Purple200)
        assertEquals(Color(0xFF6200EE), Purple500)
        assertEquals(Color(0xFF3700B3), Purple700)
        assertEquals(Color(0xFF03DAC5), Teal200)
        assertEquals(Color(0xFFFF8D55), Orange)
        assertEquals(Color(0xFFE1E8F0), LightGray)
        assertEquals(Color(0xFFA5AAB0), DarkGray)
        assertEquals(Color(0xFFFFFFFF), White)
    }

    @Test
    fun testShapes() {
        val expectedShape = RoundedCornerShape(4.dp)
        assertEquals(expectedShape, Shapes.small)
    }

    @Test
    fun `Given darkTheme is active, when the theme is called, then properties for DarkColorPalette are returned`() {
        val darkTheme = true
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        assertEquals(colors.primary, Purple200)
        assertEquals(colors.primaryVariant, Purple700)
        assertEquals(colors.secondary, Teal200)
    }

    @Test
    fun `Given a typography object, when body1 style is selected, then the value returned is correct`() {
        val expectedFontSize = 16.sp
        val expectedFontFamily = FontFamily.Default
        val expectedFontWeight = FontWeight.Normal

        val typography = Typography
        val body1Style = typography.body1

        assertEquals(expectedFontSize, body1Style.fontSize)
        assertEquals(expectedFontFamily, body1Style.fontFamily)
        assertEquals(expectedFontWeight, body1Style.fontWeight)
    }
}