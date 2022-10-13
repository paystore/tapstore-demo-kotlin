package br.com.phoebus.payments_demo.ui.components.menu

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.phoebus.payments.tef.System
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.viewmodels.PurchaseViewModel
import br.com.phoebus.payments_demo.ui.viewmodels.RefundViewModel
import br.com.phoebus.payments_demo.utils.Identification
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@Composable
fun PaymentContent(context: Context) {

    var inputCaptureValue by rememberSaveable { mutableStateOf("") }
    val locale = Locale("pt", "BR")
    val currency = Currency.getInstance(locale)
    val cleanString = inputCaptureValue.replace("[${currency.symbol},.]".toRegex(), "")
    val parsed = if (cleanString.isNotEmpty()) {
        cleanString.filter { !it.isWhitespace() }.toDouble()
    } else {
        0.00
    }
    val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100)

    val purchaseViewModel = viewModel<PurchaseViewModel>()
    var validCaptureValue by rememberSaveable { mutableStateOf(false) }
    val refundViewModel = viewModel<RefundViewModel>()
    val refreshPayments: () -> Unit = {
        refundViewModel.getItemsFinancialData()
    }

    Column(
        modifier = Modifier.wrapContentHeight(),
        Arrangement.spacedBy(15.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            value = formatted,
            onValueChange = {
                inputCaptureValue = it
                validCaptureValue = false
            },
            label = {
                Text(
                    "Valor",
                    fontSize = 14.sp
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                MaterialTheme.colors.primary,
                focusedBorderColor = MaterialTheme.colors.primaryVariant
            )
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                if (!inputCaptureValue.isBlank()) {
                    actionPayment(
                        context,
                        purchaseViewModel,
                        cleanString.filter { !it.isWhitespace() },
                        success = {
                            null
                        },
                        fail = {
                            null
                        },
                        finalize = {
                            null
                        },
                        refreshPayments
                    )
                }

            },
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.onPrimary
            )
        ) {
            Text("Pagar", fontSize = 15.sp)
        }
    }
}

private fun actionPayment(
    context: Context,
    purchaseViewModel: PurchaseViewModel,
    value: String,
    success: (() -> Unit)? = null,
    fail: (() -> Unit)? = null,
    finalize: (() -> Unit)? = null,
    refresh: (() -> Unit)? = null
) {
    System.updateDeviceInfo(context)
    purchaseViewModel.paymentExecute(
        value.toBigDecimal().toLong(),
        sucess = {
            UI.approvedScreen(
                context,
                "Transação Aprovada".uppercase(),
                "Pagamento finalizado",
                "Comprovante",
                it,
                Identification.basicRequest
            )
            success?.let {
                success()
            }
            refresh?.let {
                refresh()
            }
        },
        fail = { code, message ->
            UI.errorScreen(context, code, message, Identification.basicRequest)
            fail?.let {
                fail()
            }
        },
        finalize = finalize
    )
}