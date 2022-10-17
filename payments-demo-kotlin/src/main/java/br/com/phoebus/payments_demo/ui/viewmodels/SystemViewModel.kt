package br.com.phoebus.payments.mobile.presentation.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import br.com.phoebus.payments.tef.BuildConfig
import br.com.phoebus.payments.tef.request.BasicRequest
import br.com.phoebus.payments.tef.enumeration.OperationResultEnum
import br.com.phoebus.payments.tef.enumeration.ApplicationStatus
import br.com.phoebus.payments.tef.Config
import br.com.phoebus.payments.tef.System
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.theme.Color
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SystemViewModel(application: Application) : AndroidViewModel(application) {

    fun systemInit(
        context: Context, basicRequest: BasicRequest,
        readyStatus: () -> Unit,
        installStatus: () -> Unit,
        fail: (code: String, message: String) -> Unit
    ) {
        UI.init(context, Color.MainPallete, Identification.basicRequest, BuildConfig.BUILD_TYPE)

        val response = System.init(Identification.basicRequest, context)

        if (response.result == OperationResultEnum.SUCCESS) {
            Config.setPayStoreUrl(Identification.basicRequest, "http://177.69.97.18:6632")
            when (Config.getStatus(basicRequest).value) {
                ApplicationStatus.READY -> readyStatus()
                else -> installStatus()
            }
        } else {
            fail(response.errorCode, response.errorMessage)
        }

    }

    fun systemFinish() {
        UI.finish()

        System.finish(Identification.basicRequest)
    }

    fun resetData(
        context: Context,
        sucess: (() -> Unit)? = null,
        fail: ((code: String, message: String) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val response = System.resetData(Identification.basicRequest)

                if (response.result == OperationResultEnum.SUCCESS) {
                    System.finish(Identification.basicRequest)
                    System.init(Identification.basicRequest, context)

                    if (response.result == OperationResultEnum.SUCCESS) {
                        Config.setPayStoreUrl(
                            Identification.basicRequest,
                            "http://177.69.97.18:6632"
                        )
                    } else {
                        fail?.let { it(response.errorCode, response.errorMessage) }
                    }

                    sucess?.let { it() }
                } else {
                    fail?.let { it(response.errorCode, response.errorMessage) }
                }
            }
        }
    }

}