package br.com.phoebus.payments_demo.ui.viewmodels

import android.app.Application
import android.content.Context
import br.com.phoebus.payments.core.financial.Financial
import br.com.phoebus.payments.core.payment.CancelRequestData
import br.com.phoebus.payments.core.payment.PaymentRequestData
import br.com.phoebus.payments.platform.PlatformLog
import br.com.phoebus.payments.tef.enumeration.OperationResultEnum
import br.com.phoebus.payments.tef.Purchase
import br.com.phoebus.payments.tef.request.CancelPendingRequest
import br.com.phoebus.payments.tef.request.CancelRequest
import br.com.phoebus.payments.tef.response.CancelResponse
import br.com.phoebus.payments.tef.request.PaymentPendingRequest
import br.com.phoebus.payments.tef.request.PaymentRequest
import br.com.phoebus.payments.tef.response.PaymentResponse
import br.com.phoebus.payments.tef.System
import com.br.persistence.entity.financial.Cancel
import com.br.persistence.entity.financial.Payment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PurchaseViewModel(
    application: Application
) : GenericViewModel(application) {

    fun paymentExecute(
        context: Context,
        value: Long,
        sucess: ((payment: Payment) -> Unit)? = null,
        fail: ((code: String, message: String) -> Unit)? = null,
        finalize: (() -> Unit)? = null
    ) {

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val responsePayment: PaymentResponse = Purchase.paymentExecute(
                    context = context,
                    PaymentRequest(
                        appIdentification,
                        appCredentials,
                        PaymentRequestData(
                            value = value,
                            apiVersion = System.version()
                        )
                    )
                )

                if (responsePayment.result == OperationResultEnum.SUCCESS) {
                    val responseConfirmation = Purchase.paymentConfirmAsync(
                        PaymentPendingRequest(
                            appIdentification,
                            appCredentials,
                            responsePayment.payment!!
                        )
                    )

                    if (responseConfirmation.result == OperationResultEnum.SUCCESS) {
                        sucess?.let { it(responsePayment.payment!!) }
                    } else {
                        handleResult(responseConfirmation, sucess = {}, fail, null)
                    }
                } else {
                    if (responsePayment.result != OperationResultEnum.TRANSACTION_HOST_DENIED) {
                        if (responsePayment.payment != null) {
                            val responseUndo = Purchase.paymentUndoAsync(
                                PaymentPendingRequest(
                                    appIdentification,
                                    appCredentials,
                                    responsePayment.payment!!
                                )
                            )

                            if (responseUndo.result != OperationResultEnum.SUCCESS) {
                                handleResult(responseUndo, null, fail, null)
                            }
                        }
                    }

                    if (responsePayment.result != OperationResultEnum.INPUT_ABORT && responsePayment.result != OperationResultEnum.INPUT_TIMEOUT) {
                        handleResult(responsePayment, null, fail, null)
                    }

                    finalize?.let {
                        it()
                    }
                }
            } catch (e: Exception) {
                PlatformLog.e(null, null, e)
            } finally {
            }
        }
    }

    fun cancelExecute(
        context: Context,
        payment: Financial,
        sucess: ((cancel: Cancel) -> Unit)? = null,
        fail: ((code: String, message: String) -> Unit)? = null,
        finalize: (() -> Unit)? = null
    ) {

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val responseCancel: CancelResponse = Purchase.cancelExecute(
                    context = context,
                    CancelRequest(
                        appIdentification,
                        appCredentials,
                        CancelRequestData(
                            value = payment.value,
                            originalHostNsu = payment.acquirerNsu,
                            originalHostTrnsDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(
                                payment.date
                            ),
                            productType = payment.productType,
                            apiVersion = System.version()
                        )
                    )
                )

                if (responseCancel.result == OperationResultEnum.SUCCESS) {
                    val responseConfirmation = Purchase.cancelConfirm(
                        CancelPendingRequest(
                            appIdentification,
                            appCredentials,
                            responseCancel.cancel!!
                        )
                    )

                    if (responseConfirmation.result == OperationResultEnum.SUCCESS) {
                        sucess?.let { it(responseCancel.cancel!!) }
                    } else {
                        handleResult(responseConfirmation, sucess = {}, fail, null)
                    }
                } else {
                    if (responseCancel.result != OperationResultEnum.TRANSACTION_HOST_DENIED) {
                        if (responseCancel.cancel != null) {
                            val responseUndo = Purchase.cancelUndo(
                                CancelPendingRequest(
                                    appIdentification,
                                    appCredentials,
                                    responseCancel.cancel!!
                                )
                            )

                            if (responseUndo.result != OperationResultEnum.SUCCESS) {
                                handleResult(responseUndo, null, fail, null)
                            }
                        }
                    }

                    if (responseCancel.result != OperationResultEnum.INPUT_ABORT && responseCancel.result != OperationResultEnum.INPUT_TIMEOUT) {
                        handleResult(responseCancel, null, fail, null)
                    }

                    finalize?.let {
                        it()
                    }
                }
            } catch (e: Exception) {
                PlatformLog.e(null, null, e)
            } finally {
            }
        }
    }

}