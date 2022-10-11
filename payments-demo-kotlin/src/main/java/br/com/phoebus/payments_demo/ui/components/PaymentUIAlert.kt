package br.com.phoebus.payments_demo.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

object PaymentUIAlert {

    @Composable
    fun dialog(
        title: String,
        message: String,
        dismiss: String,
        dialog: MutableState<Boolean>,
        onDismiss: () -> Unit
    ) {
        if (dialog.value) {
            AlertDialog(
                onDismissRequest = { dialog.value = false },
                title = { Text(text = title) },
                text = { Text(message) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialog.value = false
                            onDismiss()
                        }) {
                        Text(dismiss)
                    }
                }
            )
        }
    }

}