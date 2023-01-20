package br.com.phoebus.payments_demo.ui.components.menu

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.phoebus.payments.core.financial.Financial
import br.com.phoebus.payments.core.types.TransactionTypeEnum
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.components.Payment
import br.com.phoebus.payments_demo.ui.viewmodels.PurchaseViewModel
import br.com.phoebus.payments_demo.ui.viewmodels.RefundViewModel
import br.com.phoebus.payments_demo.utils.Identification

@Composable
fun RefunContent(context: Context) {

    val refundViewModel = viewModel<RefundViewModel>()
    val purchaseViewModel = viewModel<PurchaseViewModel>()

    refundViewModel.getItemsFinancialData()

    LazyColumn(
        modifier = Modifier.height(350.dp),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        itemsIndexed(
            refundViewModel.financialData.value.orEmpty(),
            { _, it -> it.id }) { _, item ->
            Payment(
                brandName = item.brandNamePayStore,
                brandId = item.brandIdPayStore,
                pan = item.panMasked,
                value = item.value,
                date = item.date
            ) {
                purchaseViewModel.cancelExecute(
                    payment = item,
                    sucess = {
                        UI.approvedScreen(
                            context,
                            "Estorno Finalizado",
                            "Estorno aprovado",
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
        }
    }
}