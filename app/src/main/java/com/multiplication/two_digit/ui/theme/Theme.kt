package com.multiplication.two_digit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Yellow80,
    secondary = Yellow40,
    background = Color.Black,
    surface = Black80,
    onPrimary = Color.Black,
    onBackground = Yellow80,
    onSurface = Yellow80
)

@Composable
fun MultiplicationAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}