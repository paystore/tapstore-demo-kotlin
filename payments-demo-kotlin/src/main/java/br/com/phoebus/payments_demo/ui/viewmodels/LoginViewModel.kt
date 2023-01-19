package br.com.phoebus.payments_demo.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.phoebus.payments_demo.ui.components.common.ValidateEmail
import br.com.phoebus.payments_demo.ui.components.common.ValidateFone
import br.com.phoebus.payments_demo.ui.components.common.ValidateToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePhone: ValidateFone = ValidateFone(),
    private val validateToken: ValidateToken = ValidateToken()
) : ViewModel() {


    val state: MutableLiveData<LoginFormState>
        get() = _state

    private var _state: MutableLiveData<LoginFormState> = MutableLiveData(LoginFormState())

    val stateToken: MutableLiveData<LoginFormTokenState>
        get() = _stateToken

    private var _stateToken: MutableLiveData<LoginFormTokenState> =
        MutableLiveData(LoginFormTokenState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                _state.value = _state.value!!.copy(
                    email = event.email,
                    emailError = ""
                )
            }
            is LoginFormEvent.FoneChanged -> {
                _state.value = _state.value!!.copy(
                    fone = event.fone,
                    foneError = ""
                )
            }

            is LoginFormEvent.TokenChanged -> {
                _stateToken.value = _stateToken.value!!.copy(
                    token = event.token,
                    tokenError = ""
                )
            }

            is LoginFormEvent.SubmitToken -> {
                submitTokenData()
            }

            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }


    private fun submitData() {
        val emailResult = validateEmail.execute(_state.value!!.email)
        val foneResult = validatePhone.execute(_state.value!!.fone)

        val hasError = listOf(
            emailResult,
            foneResult
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value!!.copy(
                emailError = emailResult.errorMessage,
                foneError = foneResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun submitTokenData() {
        val tokenResult = validateToken.execute(_stateToken.value!!.token)

        val hasError = listOf(
            tokenResult
        ).any { !it.successful }

        if (hasError) {
            _stateToken.value = _stateToken.value!!.copy(
                tokenError = tokenResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.SuccessToken)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object SuccessToken : ValidationEvent()
    }
}

data class LoginFormState(
    val email: String = "",
    val emailError: String = "",
    val fone: String = "",
    val foneError: String = ""
)

data class LoginFormTokenState(
    val token: String = "",
    val tokenError: String = ""
)

sealed class LoginFormEvent {
    data class EmailChanged(val email: String) : LoginFormEvent()
    data class FoneChanged(val fone: String) : LoginFormEvent()
    data class TokenChanged(val token: String) : LoginFormEvent()

    object Submit : LoginFormEvent()
    object SubmitToken : LoginFormEvent()
}

