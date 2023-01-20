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
import br.com.phoebus.payments_demo.BuildConfig.apiVersionName
import br.com.phoebus.payments_demo.ui.components.ActionCard
import br.com.phoebus.payments_demo.ui.components.menu.*
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation
import br.com.phoebus.payments_demo.ui.theme.Color

@Composable
fun MainScreen(navController: NavController, context: Context) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demo") },
                backgroundColor = Color.primary,
                actions = {
                    IconButton(onClick = { navController.navigate(Navigation.SettingsScreen.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colors.background

                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "Versão da api: $apiVersionName")
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
            ActionCard(icon = Icons.Outlined.CreditCard, title = "Pagamento por Aproximação") {
                PaymentContent(context = context)
            }
            ActionCard(icon = Icons.Outlined.Update, title = "Estorno") {
                RefunContent(context = context)
            }
            ActionCard(icon = Icons.Outlined.Language, title = "Teste de Comunicação") {
                HealthTestContent()
            }
            ActionCard(icon = Icons.Outlined.ColorLens, title = "UI/UX") {
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