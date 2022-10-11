package br.com.phoebus.payments_demo.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MainTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = MainPallete,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}