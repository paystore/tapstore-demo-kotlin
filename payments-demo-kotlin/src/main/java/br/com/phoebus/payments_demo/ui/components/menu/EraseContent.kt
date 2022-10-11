package br.com.phoebus.payments_demo.ui.components.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.phoebus.payments.mobile.presentation.viewmodels.SystemViewModel
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EraseContent(context: Context, navController: NavController) {

    val composableScope = rememberCoroutineScope()
    val systemViewModel = viewModel<SystemViewModel>()

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = {
            actionResetData(
                coroutineScope = composableScope,
                context = context,
                systemViewModel = systemViewModel,
                navController = navController
            )
        },
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary
        )
    ) {
        Text("Limpar dados", fontSize = 15.sp)
    }
}

private fun actionResetData(
    coroutineScope: CoroutineScope,
    context: Context,
    systemViewModel: SystemViewModel,
    navController: NavController
) {
    systemViewModel.resetData(context,
        sucess = {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Finalizado",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Navigation.MainScreen.route)
                }
            }
        },
        fail = { code, message ->
            UI.errorScreen(
                context,
                code,
                message,
                Identification.basicRequest
            )
        }
    )
}