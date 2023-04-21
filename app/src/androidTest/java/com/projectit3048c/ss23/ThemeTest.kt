package com.projectit3048c.ss23

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import com.projectit3048c.ss23.ui.theme.DarkColorPalette
import com.projectit3048c.ss23.ui.theme.LightColorPalette
import com.projectit3048c.ss23.ui.theme.ProjectIT3048CTheme
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

/**
 * Test class for the [ProjectIT3048CTheme] composable function
 * Tests if the light theme is applied by default
 */
class ThemeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun projectIT3048CTheme_isLightByDefault() {
        composeTestRule.setContent {
            var isDarkTheme = false
            val testContent: @Composable () -> Unit = {}
            CompositionLocalProvider(LocalContentColor provides Color.Black) {
                ProjectIT3048CTheme(darkTheme = isDarkTheme) {
                    testContent()
                }
            }
            val colorPalette = MaterialTheme.colors
            val Purple500 = Color(0xFF6200EE)
            TestCase.assertEquals(colorPalette.primary, Purple500)
        }
    }
}