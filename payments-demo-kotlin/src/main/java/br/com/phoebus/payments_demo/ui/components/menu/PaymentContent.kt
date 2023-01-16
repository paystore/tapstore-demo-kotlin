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
import br.com.phoebus.payments.core.financial.Financial
import br.com.phoebus.payments.core.types.TransactionTypeEnum
import br.com.phoebus.payments.tef.System
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.theme.Color
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
                Color.primary,
                focusedBorderColor = Color.primaryVariant
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
                    )
                }

            },
            colors = ButtonDefaults.buttonColors(
                Color.primary,
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
        context = context,
        value.toBigDecimal().toLong(),
        sucess = {
            UI.approvedScreen(
                context,
                "Transação Aprovada".uppercase(),
                "Pagamento finalizado",
                "Comprovante",
                Financial(
                    id = it.id,
                    type = TransactionTypeEnum.PAYMENT,
                    date = it.date,
                    value = it.value,
                    productId = it.productId,
                    productType = it.productType,
                    brandIdPhast = it.brandIdPhast,
                    brandNamePhast = it.brandNamePhast,
                    brandIdPayStore = it.brandIdPayStore,
                    brandNamePayStore = it.brandNamePayStore,
                    brandName = it.brandNameResponse,
                    paystoreAcquirerLogicId = it.paystoreAcquirerLogicId,
                    nsu = it.nsu,
                    acquirerNsu = it.acquirerNsu,
                    hostNsu = it.hostNsu,
                    paymentStatus = it.paymentStatus,
                    panMasked = it.panMasked,
                    uuid = it.uuid,
                    merchantReceipt = it.merchantReceipt,
                    clientReceipt = it.clientReceipt,
                    url = "http://177.69.97.18:9313/paystore/brand/visa/2e44ff66052fc7aaa9f6416fbae2e1af.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=04RX9OSWK4KZSW9GILMY%2F20221101%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20221101T142323Z&X-Amz-Expires=120&X-Amz-SignedHeaders=host&X-Amz-Signature=6919ad0ee42a1ce3944c02c9aa233fe4de551acf322fb45a0f990530edd4376e"
                ),
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