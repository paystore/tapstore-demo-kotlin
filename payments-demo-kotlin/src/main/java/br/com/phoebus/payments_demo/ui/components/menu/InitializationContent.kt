package br.com.phoebus.payments_demo.ui.components.menu

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.theme.Color
import br.com.phoebus.payments_demo.ui.viewmodels.AdminViewModel
import br.com.phoebus.payments_demo.ui.viewmodels.MerchantViewModel
import br.com.phoebus.payments_demo.utils.Identification

@Composable
fun InitializationContent(context: Context) {

    val adminViewModel = viewModel<AdminViewModel>()

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = {
            actionInitialize(context = context,
                adminViewModel = adminViewModel)
        },
        colors = ButtonDefaults.buttonColors(
            Color.primary,
            MaterialTheme.colors.onPrimary
        )
    ) {
        Text("Inicializar", fontSize = 15.sp)
    }

}

private fun actionInitialize(
    context: Context,
    adminViewModel: AdminViewModel,
) {
    adminViewModel.initialization(
        sucess = {
            UI.successScreen(
                context, "Inicializando",
                Identification.basicRequest
            )
        },
        fail = { code, message ->
            UI.errorScreen(context, code, message, Identification.basicRequest)
        }
    )
}