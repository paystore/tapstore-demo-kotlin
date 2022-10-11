package br.com.phoebus.payments_demo.ui.presentation.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments.ui.components.Background
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation
import br.com.phoebus.payments_demo.ui.viewmodels.AdminViewModel
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@Composable
fun LoginScreen(navController: NavController, context: Context) {

    val scope = rememberCoroutineScope()
    var inputCaptureToken by rememberSaveable { mutableStateOf("") }
    var validCaptureToken by rememberSaveable { mutableStateOf(false) }
    val adminViewModel = viewModel<AdminViewModel>()
    val captureToken = remember { mutableStateOf(false) }

    Background() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize().padding(15.dp).clip(RoundedCornerShape(25.dp))
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                        .padding(horizontal = 25.dp)
                        .padding(top = 1.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = inputCaptureToken,
                        onValueChange = {
                            inputCaptureToken = it
                            validCaptureToken = false
                        },
                        label = {
                            Text(
                                "Token Paystore",
                                fontSize = 14.sp
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            MaterialTheme.colors.primary,
                            focusedBorderColor = MaterialTheme.colors.primaryVariant
                        )
                    )
                    if (validCaptureToken) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            fontSize = 12.sp,
                            text = "Campo Obrigatório*",
                            color = MaterialTheme.colors.error,
                            textAlign = TextAlign.Start,
                        )
                    }
                    Button(
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        onClick = {
                            if (inputCaptureToken.isNotEmpty()) {
                                tryInstall(
                                    scope,
                                    context,
                                    adminViewModel,
                                    navController,
                                    captureToken = {
                                        captureToken.value = true
                                    },
                                    token = inputCaptureToken
                                )
                            } else {
                                validCaptureToken = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.onPrimary
                        )
                    ) {
                        Text("Enviar", fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

private fun tryInstall(
    coroutineScope: CoroutineScope,
    context: Context,
    adminViewModel: AdminViewModel,
    navController: NavController,
    captureToken: () -> Unit,
    token: String = ""
) {
    adminViewModel.instalation(
        sucess = {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    navController.navigate(Navigation.MainScreen.route)
                }
            }

            CoroutineScope(Dispatchers.Default).launch {
                UI.successScreen(
                    context,
                    "Inicializado",
                    Identification.basicRequest
                )
            }
        },
        fail = { code, message ->
            UI.errorScreen(
                context, code = if (code == "400") {
                    "PA-$code"
                } else {
                    code
                }, message, Identification.basicRequest
            )
        },
        captureToken = {
            captureToken()
        },
        token = token
    )
}
