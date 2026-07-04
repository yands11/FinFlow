package com.finflow.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Color(0xFF003829),
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = OnGreenContainerDark,
    secondary = GreenGrey80,
    secondaryContainer = Color(0xFF2E4F44),
    onSecondaryContainer = GreenGrey80,
    tertiary = Teal80,
    surface = Color(0xFF121C18),
    surfaceVariant = Color(0xFF3B4F47),
    onSurfaceVariant = Color(0xFFBFC9C3),
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = GreenContainer,
    onPrimaryContainer = OnGreenContainer,
    secondary = GreenGrey40,
    secondaryContainer = GreenContainer,
    onSecondaryContainer = OnGreenContainer,
    tertiary = Teal40,
    surface = GreenSurface,
    surfaceVariant = GreenSurfaceVariant,
    onSurfaceVariant = Color(0xFF3F4F48),
)

@Composable
fun FinFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
