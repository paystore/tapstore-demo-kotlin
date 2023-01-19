package br.com.phoebus.payments_demo.ui.components.common

class ValidateFone {

    fun execute(fone: String): ValidationResult {
        if(fone.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Campo telefone obrigatório"
            )
        }
        if (fone.length < MaskPhoneNumber.INPUT_LENGTH_PHONE_NUMBER){
            return ValidationResult(
                successful = false,
                errorMessage = "Telefone inválido"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}