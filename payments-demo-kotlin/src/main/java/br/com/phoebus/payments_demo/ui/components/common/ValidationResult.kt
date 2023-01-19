package br.com.phoebus.payments_demo.ui.components.common

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String = ""
)
