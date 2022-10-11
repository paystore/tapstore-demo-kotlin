package br.com.phoebus.payments_demo.ui.viewmodels

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.phoebus.payments.paystore.PayStoreModuleService
import br.com.phoebus.payments.paystore.rest.PayStoreRespApiError
import br.com.phoebus.payments.phast.PhastModuleService
import br.com.phoebus.payments.phast.rest.PhastRespApiError
import br.com.phoebus.payments.tef.Admin
import br.com.phoebus.payments.tef.request.HealthCheckRequest
import br.com.phoebus.payments.tef.request.InitializationRequest
import br.com.phoebus.payments.tef.request.InstallationRequest
import br.com.phoebus.payments.tef.request.SyncRequest
import br.com.phoebus.payments.tef.response.HealthCheckResponse
import br.com.phoebus.payments.ui.models.GroupInfo
import br.com.phoebus.payments.ui.models.GroupInfoDetail
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class AdminViewModel(
    application: Application
) : GenericViewModel(application) {

    private val _initialization = MutableLiveData<Boolean>()
    val initialization: LiveData<Boolean> get() = _initialization

    private val _instalation = MutableLiveData<Boolean>()
    val instalation: LiveData<Boolean> get() = _instalation

    private val _sync = MutableLiveData<Boolean>()
    val sync: LiveData<Boolean> get() = _sync

    private val _healthCheck = MutableLiveData<Boolean>()
    val healthCheck: LiveData<Boolean> get() = _healthCheck

    private val _healthCheckResponse = MutableLiveData<HealthCheckResponse>()
    val healthCheckResponse: MutableLiveData<HealthCheckResponse>
        get() = _healthCheckResponse

    private val _healthCheckFeedback = MutableLiveData<Boolean>()
    val healthCheckFeedback: MutableLiveData<Boolean>
        get() = _healthCheckFeedback


    private val _healthCheckData = MutableLiveData<List<GroupInfo>>()
    val healthCheckData: MutableLiveData<List<GroupInfo>>
        get() = _healthCheckData

    private val context = getApplication<Application>().applicationContext

    fun initialization(sucess: (() -> Unit)? = null, fail: ((code: String, message: String) -> Unit)? = null) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                handleResult(
                    Admin.initialize(
                    InitializationRequest(
                        appIdentification,
                        appCredentials
                    )
                ), sucess, fail, _initialization)
            }
        }
    }

    fun instalation(sucess: (() -> Unit)? = null, fail: ((code: String, message: String) -> Unit)? = null, captureToken: () -> Unit, token: String = "") {
        CoroutineScope(Dispatchers.Default).launch {
            handleResult(
                Admin.installation(
                InstallationRequest(
                    appIdentification,
                    appCredentials,
                    token
                ),
            ), sucess, fail, _instalation, captureToken)
        }
    }

    fun healthCheck(sucess: (() -> Unit)? = null, fail: ((code: String, message: String) -> Unit)? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            handleResultHealthCheck(
                Admin.healthCheck(
                    HealthCheckRequest(
                        appIdentification,
                        appCredentials
                    )
                ), sucess, fail, _healthCheckResponse)
        }
    }

    fun sync(sucess: (() -> Unit)? = null, fail: ((code: String, message: String) -> Unit)? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            handleResult(
                Admin.sync(
                SyncRequest(
                    appIdentification,
                    appCredentials
                )
            ), sucess, fail, _sync)
        }
    }

    fun export(sucess: (() -> Unit)? = null, fail: ((code: String, message: String) -> Unit)? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            handleResult(Admin.export(Identification.basicRequest), sucess, fail, null)
        }
    }

    suspend fun executeHealthCheck(){
        val response = HealthCheckResponse(false, 0, "", false, 0, "")
        healthCheckFeedback.value = true
        //PayStore
        try{
            response.paystoreResultTime = measureTimeMillis {
                PayStoreModuleService.authentication()
                PayStoreModuleService.clock()

                response.paystoreResult = true
            }
        } catch (error: PayStoreRespApiError){
            response.paystoreResult = false
            response.paystoreResultError =
                error.paystoreErrorCode + "\n" + error.paystoreErrorMessage
        } catch (e: Exception){
            response.paystoreResultError = e.message.toString()
        }
        //Phast
        try{
            response.phastResultTime = measureTimeMillis {
                PayStoreModuleService.processAcquirers(true) { id, packageName ->
                    PhastModuleService.healthCheck(id, packageName)
                }

                response.phastResult = true
            }
        } catch (error: PhastRespApiError){
            response.phastResult = false
            response.phastResultError =
                error.phastErrorCode + "\n" + error.phastErrorMessage
        } catch (e: Exception){
            response.phastResultError = e.message.toString()
        }
        delay(3000)
        _healthCheckResponse.value = response
        healthCheckFeedback.value = false
    }
}