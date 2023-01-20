package br.com.phoebus.payments_demo.ui.presentation.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.phoebus.payments.config.ConfigModuleService
import br.com.phoebus.payments.platform.Platform
import br.com.phoebus.payments.ui.components.DefaultOutlineTextField
import br.com.phoebus.payments_demo.ui.theme.Color

@Composable
fun SettingsScreen(navController: NavHostController, context: Context) {
    val checkedDevModeState = remember { mutableStateOf(Platform.getCheckDevMode()) }
    val checkedRootModeState = remember { mutableStateOf(Platform.getCheckRootMode()) }
    var valueStartStateParamInitialize = remember {
        mutableStateOf(
            ConfigModuleService.getPeriodUntilInitialize().toString()
        )
    }
    var valueStartStateParamPurge = remember {
        mutableStateOf(
            ConfigModuleService.getPeriodUntilPurge().toString()
        )
    }
    val maxCharParam = 2
    val stateScroll = rememberScrollState()
    val context: Context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações da API ") },
                backgroundColor = Color.primary,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.background

                        )
                    }
                }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .verticalScroll(stateScroll),

            ) {
            Text(text = "Informações sobre o dispositivo:")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Modelo: " + Build.MODEL)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Fabricante: " + Build.MANUFACTURER)
            }
            Divider(
                modifier = Modifier.height(1.dp),
                thickness = 1.dp
            )
            Text(text = "Inicialização automática (em minutos):")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                DefaultOutlineTextField(
                    modifier = Modifier
                        .padding(end = 15.dp),
                    value = valueStartStateParamInitialize.value,
                    onValueChange = {
                        if (it.length <= maxCharParam) valueStartStateParamInitialize.value = it
                    },
                    label = {
                        Text(
                            "Valor inicial",
                            fontSize = 14.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(

                        Color.primary,
                        focusedBorderColor = MaterialTheme.colors.primaryVariant
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp),
                    onClick = {
                        if (valueStartStateParamInitialize.value.toLong() > 15) {
                            ConfigModuleService.setPeriodUntilInitialize(
                                valueStartStateParamInitialize.value.toLong()
                            )
                            Toast.makeText(
                                context,
                                "Valor alterado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Valor abaixo do mínimo permitido (15)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text(text = "Alterar valor")
                }
            }
            Divider(
                modifier = Modifier.height(1.dp),
                thickness = 1.dp
            )
            Text(text = "Expurgo automático (em meses):")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                DefaultOutlineTextField(
                    modifier = Modifier
                        .padding(end = 15.dp),
                    value = valueStartStateParamPurge.value,
                    onValueChange = {
                        if (it.length <= maxCharParam) valueStartStateParamPurge.value = it
                    },
                    label = {
                        Text(
                            "Valor inicial",
                            fontSize = 14.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        Color.primary,
                        focusedBorderColor = MaterialTheme.colors.primaryVariant
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp),
                    onClick = {
                        ConfigModuleService.setPeriodUntilPurge(valueStartStateParamPurge.value.toLong())
                        Toast.makeText(context, "Valor alterado com sucesso!", Toast.LENGTH_SHORT)
                            .show()
                    }
                ) {
                    Text(text = "Alterar valor")
                }
            }
            Divider(
                modifier = Modifier.height(1.dp),
                thickness = 1.dp
            )
        }

    }

}