package br.com.phoebus.payments_demo.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.phoebus.payments.tef.Config
import br.com.phoebus.payments.tef.enumeration.ApplicationStatus
import br.com.phoebus.payments.tef.enumeration.OperationResultEnum
import br.com.phoebus.payments.tef.response.BasicResponse
import br.com.phoebus.payments.tef.response.HealthCheckResponse
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.launch

abstract class GenericViewModel(
    application: Application
) : AndroidViewModel(application) {

    val appCredentials = Identification.appCredentials
    val appIdentification = Identification.appIdentification

    fun handleResult(response: BasicResponse, sucess: (() -> Unit)?, fail: ((code: String, message: String) -> Unit)?, mutable: MutableLiveData<Boolean>?, captureToken: (() -> Unit)? = null) {
        val applicationContext = getApplication<Application>().applicationContext

        when(response.result) {
            OperationResultEnum.SUCCESS -> {
                if (Config.getStatus(Identification.basicRequest).value!! == ApplicationStatus.READY) {
                    viewModelScope.launch {
                        mutable?.let { it.value = true }
                    }
                    sucess?.let { it() }
                } else {
                    fail?.let { it(response.errorCode, response.errorMessage) }
                }
            }
            OperationResultEnum.NEED_TOKEN -> {
                captureToken?.let {
                    captureToken()
                }
            }
            OperationResultEnum.CONNECT_FAIL -> {
                fail?.let { it(response.errorCode,"Conection Fail") }
            }
            OperationResultEnum.CONNECTION_FAIL -> {
                fail?.let { it(response.errorCode, "Conection Fail") }
            }
            else -> {
                fail?.let { it(response.errorCode, response.errorMessage) }
            }
        }
    }

    fun handleResultHealthCheck(response: HealthCheckResponse, sucess: (() -> Unit)?, fail: ((code: String, message: String) -> Unit)?, mutable: MutableLiveData<HealthCheckResponse>?) {
        val applicationContext = getApplication<Application>().applicationContext

        when(response.result) {
            OperationResultEnum.SUCCESS -> {
                if (Config.getStatus(Identification.basicRequest).value!! == ApplicationStatus.READY) {
                    viewModelScope.launch {
                        mutable?.let { it.value = response}
                    }
                    sucess?.let { it() }
                }  else {
                    fail?.let { it(response.errorCode, response.errorMessage) }
                }
            }
            OperationResultEnum.CONNECT_FAIL -> {
                fail?.let { it(response.errorCode, "Conection Fail") }
            }
            OperationResultEnum.CONNECTION_FAIL -> {
                fail?.let { it(response.errorCode, "Conection Fail") }
            }
            else -> {
                fail?.let { it(response.errorCode, response.errorMessage) }
            }
        }
    }

}