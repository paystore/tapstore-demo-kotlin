package br.com.phoebus.payments_demo.ui.presentation.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.phoebus.payments.ui.components.Background
import br.com.phoebus.payments_demo.R
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation

@Composable
fun SplashScreen(loadProcess: (() -> Unit)) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 100,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        loadProcess()
    }

    Background() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.ic_mainlogo),
                contentDescription = "Logo",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
            )
        }
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    SplashScreen {
    }
}
