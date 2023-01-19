package br.com.phoebus.payments_demo.ui.components.common

import android.util.Patterns

class ValidateEmail() {

    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Campo email obrigatório"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email inválido"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = ""
        )
    }
}