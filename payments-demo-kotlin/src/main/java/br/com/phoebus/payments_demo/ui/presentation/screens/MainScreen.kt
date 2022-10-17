package br.com.phoebus.payments_demo.ui.presentation.screens

import android.content.Context
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.phoebus.payments.tef.BuildConfig
import br.com.phoebus.payments_demo.ui.components.ActionCard
import br.com.phoebus.payments_demo.ui.components.menu.*
import br.com.phoebus.payments_demo.ui.theme.Color

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavController, context: Context) {

    var reportState by remember {
        mutableStateOf(false)
    }
    var testState by remember {
        mutableStateOf(false)
    }
    var eraseState by remember {
        mutableStateOf(false)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demo") },
                backgroundColor = Color.primary
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "Versão da api: ${BuildConfig.apiVersionName}")
                Text(
                    text = "ID do dispositivo: ${
                        Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    }"
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            ActionCard(icon = Icons.Outlined.Person, title = "Dados do Lojista") {
                merchantContent()
            }
            ActionCard(icon = Icons.Outlined.CreditCard, title = "Pagamento NFC") {
                PaymentContent(context = context)
            }
            ActionCard(icon = Icons.Outlined.Update, title = "Estorno") {
                RefunContent(context = context)
            }
            ActionCard(icon = Icons.Outlined.Language, title = "Teste de Comunicação") {
                HealthTestContent()
            }
            ActionCard(icon = Icons.Outlined.ColorLens, title = "Customização") {
                Customization(navController, context)
            }
            ActionCard(icon = Icons.Outlined.PhonelinkErase, title = "Limpar dados") {
                EraseContent(context = context, navController = navController)
            }
            ActionCard(icon = Icons.Outlined.Flag, title = "Inicializar") {
                InitializationContent(context)
            }
        }
    }
}