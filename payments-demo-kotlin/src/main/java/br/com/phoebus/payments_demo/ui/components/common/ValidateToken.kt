package br.com.phoebus.payments_demo.ui.components.common

class ValidateToken {

    fun execute(token: String): ValidationResult {
        if (token.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Campo Token obrigatório"
            )
        }
        if (token.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "Token inválido"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}