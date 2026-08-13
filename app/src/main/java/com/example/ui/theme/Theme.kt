package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = GreenPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = GreenPrimaryDark,
    onPrimaryContainer = Color(0xFFD1F2E3),
    secondary = GoldAccentLight,
    onSecondary = Color(0xFF382300),
    secondaryContainer = Color(0xFF533B00),
    onSecondaryContainer = Color(0xFFFFDF9E),
    tertiary = Color(0xFF81D4FA),
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextSecondaryDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ChipSelectedBg,
    onPrimaryContainer = ChipSelectedText,
    secondary = GoldAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE082),
    onSecondaryContainer = Color(0xFF332000),
    tertiary = Color(0xFF0288D1),
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardLight,
    onSurfaceVariant = TextSecondaryLight,
  )

@Composable
fun TeacherMateTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted Nigerian emerald theme by default
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  TeacherMateTheme(darkTheme, dynamicColor, content)
}

