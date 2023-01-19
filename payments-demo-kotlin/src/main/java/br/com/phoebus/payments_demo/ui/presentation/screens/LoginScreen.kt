package br.com.phoebus.payments_demo.ui.presentation.screens

import android.content.Context
import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.phoebus.payments.platform.Platform
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments.ui.components.Background
import br.com.phoebus.payments.ui.components.OutlineTextFieldWithErrorView
import br.com.phoebus.payments_demo.ui.components.SmsCodeView
import br.com.phoebus.payments_demo.ui.components.common.MaskPhoneNumber
import br.com.phoebus.payments_demo.ui.components.common.MaskVisualTransformation
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation
import br.com.phoebus.payments_demo.ui.viewmodels.AdminViewModel
import br.com.phoebus.payments_demo.ui.viewmodels.LoginFormEvent
import br.com.phoebus.payments_demo.ui.viewmodels.LoginViewModel
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController, context: Context) {

    val adminViewModel = viewModel<AdminViewModel>()
    val captureToken = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val viewModel = viewModel<LoginViewModel>()
    val state by viewModel.state.observeAsState()
    val stateToken by viewModel.stateToken.observeAsState()
    val buttonsEnable = remember { mutableStateOf(true) }

    val startTime: Long = 120 * 1000
    val sendCode = rememberSaveable { mutableStateOf(false) }

    val timeData = rememberSaveable {
        mutableStateOf(startTime)
    }

    val minutes = rememberSaveable {
        mutableStateOf(startTime / 1000 / 60)
    }

    val seconds = rememberSaveable {
        mutableStateOf(startTime / 1000 % 60)
    }

    val countDownTimer =
        object : CountDownTimer(startTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeData.value = timeData.value - 1000
                minutes.value = timeData.value / 1000 / 60
                seconds.value = timeData.value / 1000 % 60
            }

            override fun onFinish() {
                sendCode.value = true
                timeData.value = startTime
            }
        }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LoginViewModel.ValidationEvent.SuccessToken -> {
                    tryInstall(
                        context,
                        adminViewModel,
                        navController,
                        captureToken = {
                            captureToken.value = true
                            buttonsEnable.value = true
                        },
                        token = stateToken!!.token,
                        failInstall = {

                        }
                    )
                }
                is LoginViewModel.ValidationEvent.Success -> {
                    adminViewModel.topRegister(
                        merchantEmail = state!!.email,
                        terminalPhoneNumber = state!!.fone,
                        sucess = {
                            tryInstall(
                                context,
                                adminViewModel,
                                navController,
                                captureToken = {
                                    countDownTimer.start()
                                    captureToken.value = true
                                    buttonsEnable.value = true
                                },
                                token = stateToken!!.token,
                                failInstall = {
                                }
                            )
                        },
                        fail = { code, message ->
                            UI.errorScreen(context, code, message, Identification.basicRequest)
                        }
                    )
                }
            }
        }
    }

    Background {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(25.dp))
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .padding(vertical = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(visible = !captureToken.value, enter = fadeIn()) {
                        Column {
                            Text(
                                text = "Informe o E-mail",
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(bottom = 5.dp)
                                    .fillMaxWidth(),
                                color = MaterialTheme.colors.onBackground
                            )
                            OutlineTextFieldWithErrorView(
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 15.dp),
                                value = state!!.email,
                                onValueChange = {
                                    viewModel.onEvent(LoginFormEvent.EmailChanged(it))
                                },
                                isError = state!!.emailError.isNotEmpty(),
                                errorMsg = state!!.emailError,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    MaterialTheme.colors.primary,
                                    focusedBorderColor = MaterialTheme.colors.primaryVariant
                                ),
                                singleLine = true
                            )
                            Text(
                                text = "Informe o Telefone",
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(bottom = 5.dp)
                                    .fillMaxWidth(),
                                color = MaterialTheme.colors.onBackground
                            )
                            OutlineTextFieldWithErrorView(
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 15.dp),
                                value = state!!.fone,
                                onValueChange = {
                                    if (it.length <= MaskPhoneNumber.INPUT_LENGTH_PHONE_NUMBER) {
                                        viewModel.onEvent(LoginFormEvent.FoneChanged(it.filter { it.isDigit() }))
                                    }
                                },
                                isError = state!!.foneError.isNotEmpty(),
                                errorMsg = state!!.foneError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        viewModel.onEvent(LoginFormEvent.Submit)
                                        keyboardController?.hide()
                                    }
                                ),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    MaterialTheme.colors.primary,
                                    focusedBorderColor = MaterialTheme.colors.primaryVariant
                                ),
                                visualTransformation = MaskVisualTransformation(MaskPhoneNumber.MASK_PHONE_NUMBER)
                            )
                        }
                    }
                    AnimatedVisibility(visible = captureToken.value, enter = fadeIn()) {
                        Column {
                            Text(
                                "Insira o Token de instalação",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colors.onBackground
                            )
                            SmsCodeView(
                                smsCodeLength = 8,
                                smsFulled = {
                                    viewModel.onEvent(LoginFormEvent.TokenChanged(it))
                                }
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            AnimatedVisibility(
                                visible = (!sendCode.value && !Platform.isTerminalPos()),
                                enter = fadeIn()
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    // Content that needs to appear/disappear goes here:
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 12.dp),
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colors.onBackground,
                                        text = minutes.value.toString()
                                            .padStart(2, '0') + ":" + seconds.value.toString()
                                            .padStart(2, '0')
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        onClick = {
                            viewModel.onEvent(LoginFormEvent.Submit)
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
    context: Context,
    adminViewModel: AdminViewModel,
    navController: NavController,
    captureToken: () -> Unit,
    token: String = "",
    failInstall: () -> Unit
) {
    adminViewModel.instalation(
        sucess = {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Main) {
                    navController.navigate(Navigation.MainScreen.route)
                }
            }
            UI.inputCardViewModel.updateBrandTapAvailable()
            CoroutineScope(Dispatchers.Default).launch {
                UI.successScreen(
                    context,
                    "Inicializado com sucesso",
                    Identification.basicRequest
                )
            }
        },
        fail = { code, message ->
            failInstall()
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
