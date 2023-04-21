package com.projectit3048c.ss23.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

internal val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

internal val LightColorPalette = lightColors(
    primary = DarkGray,
    primaryVariant = Orange,
    secondary = LightGray,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

/**
 * Custom [MaterialTheme] that defines the color palette and other visual attributes for the app
 *
 * @param darkTheme Boolean that indicates whether the theme is currently in dark mode
 * @param content Composable function to describe UI content
 * @return A [MaterialTheme] object with  color palette, typography, shapes, and UI content
 */
@Composable
fun ProjectIT3048CTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}