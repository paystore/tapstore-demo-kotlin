package br.com.phoebus.payments_demo.ui.components.menu

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.components.Payment
import br.com.phoebus.payments_demo.ui.viewmodels.PurchaseViewModel
import br.com.phoebus.payments_demo.ui.viewmodels.RefundViewModel
import br.com.phoebus.payments_demo.utils.Identification

@Composable
fun RefunContent(context: Context) {

    val refundViewModel = viewModel<RefundViewModel>()
    val purchaseViewModel = viewModel<PurchaseViewModel>()

    LazyColumn(
        modifier = Modifier.height(350.dp),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        itemsIndexed(
            refundViewModel.financialData.value.orEmpty(),
            { _, it -> it.id }) { _, item ->
            Payment(
                brandName = item.brandName,
                brandId = item.brandId,
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
                            it,
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