package br.com.phoebus.payments_demo.ui.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object Color {

    var primary = Color(0xFF202020)
    val primaryVariant = Color(0xFF202020)
    val onPrimary = Color(0xFFFFFFFF)
    val secondary = Color(0xFFFFFFFF)
    val secondaryVariant = Color(0xFFFFFFFF)
    val onSecondary = Color(0xFF202020)
    val surface = Color(0xFFFFFFFF)
    val onSurface = Color(0xFF000000)
    val error = Color(0xffFF0000)
    val background = Color(0xFFFFFFFF)
    val onBackground = Color(0xFF202020)
    val onError = Color(0xFFFFFFFF)

    var MainPallete = lightColors(
        primary = primary,
        primaryVariant = primaryVariant,
        onPrimary = onPrimary,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        onSecondary = onSecondary,
        surface = surface,
        onSurface = onSurface,
        error = error,
        background = background,
        onBackground = onBackground,
        onError = onError
    )
}
