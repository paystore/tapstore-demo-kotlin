package br.com.phoebus.payments_demo.ui.components.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.phoebus.payments_demo.ui.theme.MainTheme
import br.com.phoebus.payments_demo.ui.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

@Composable
fun HealthTestContent() {

    val adminViewModel = viewModel<AdminViewModel>()
    val healthCheckResponse = adminViewModel.healthCheckResponse
    val composableScope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        testConnection(
            title = "Paystore",
            done = healthCheckResponse.value?.paystoreResult,
            loading = loading
        )
        testConnection(
            title = "Concentrador",
            done = healthCheckResponse.value?.phastResult,
            loading = loading
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                composableScope.launch {
                    loading = true
                    adminViewModel.executeHealthCheck()
                    loading = false
                }
            },
            colors = ButtonDefaults.buttonColors(
                br.com.phoebus.payments_demo.ui.theme.Color.primary,
                MaterialTheme.colors.onPrimary
            )
        ) {
            Text("Testar", fontSize = 15.sp)
        }
    }


}

@Composable
fun testConnection(title: String, done: Boolean?, loading: Boolean) {

    MainTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(bottom = 15.dp)
                .height(60.dp), shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF7F7F7),
            border = BorderStroke(2.dp, br.com.phoebus.payments_demo.ui.theme.Color.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title)
                AnimatedVisibility(visible = loading) {
                    LinearProgressIndicator(modifier = Modifier.padding(horizontal = 15.dp))
                }
                Icon(
                    imageVector = Icons.Outlined.Public,
                    contentDescription = "network",
                    tint = if (done == true) {
                        Color(0xFF66BB6A)
                    } else {
                        Color(0xFFEF5350)
                    }
                )
            }
        }
    }

}