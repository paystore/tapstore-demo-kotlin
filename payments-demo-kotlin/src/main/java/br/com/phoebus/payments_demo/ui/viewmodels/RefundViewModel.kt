package br.com.phoebus.payments_demo.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import br.com.phoebus.payments.core.financial.Financial
import br.com.phoebus.payments.core.financial.FinancialRequestData
import br.com.phoebus.payments.core.types.OrderTransactionType
import br.com.phoebus.payments.core.types.PaymentStatus
import br.com.phoebus.payments.tef.request.FinancialRequest
import java.util.*

class RefundViewModel(
    application: Application
) : GenericViewModel(application) {

    private var _financialData = MutableLiveData<List<Financial>>()
    val financialData: MutableLiveData<List<Financial>>
        get() = _financialData

    init {
        getItemsFinancialData()
    }

    fun getItemsFinancialData() {
        val date = Calendar.getInstance()
        date.add(Calendar.DATE, -7)
        val response = br.com.phoebus.payments.tef.Financial.execute(
            FinancialRequest(
                appIdentification,
                appCredentials,
                FinancialRequestData(
                    startDate = date.time,
                    endDate = Date(),
                    limit = 20,
                    offset = 0,
                    order = OrderTransactionType.DESCENDING,
                    status = PaymentStatus.CONFIRMED
                )
            )
        )
        if (response.data != null) {
            financialData.value = response.data!!.listFinancial
        }
    }

}