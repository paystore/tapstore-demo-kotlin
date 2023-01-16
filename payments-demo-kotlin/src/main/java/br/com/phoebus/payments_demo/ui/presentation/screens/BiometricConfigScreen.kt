package br.com.phoebus.payments_demo.ui.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import br.com.phoebus.payments.platform.Platform
import br.com.phoebus.payments.ui.components.BasicView
import br.com.phoebus.payments.ui.enums.HeaderTypeEnum
import br.com.phoebus.payments_demo.ui.theme.MainTheme

@Composable
fun BiometricConfigScreen(context: Context, activity: FragmentActivity) {
    MainTheme{
        BasicView(headerType = HeaderTypeEnum.LOGO) {
            Spacer(modifier = Modifier.padding(40.dp))
            Text(
                text= "Cadastre uma senha ou biometria no seu celular primeiro",
                fontSize = 25.sp,
                modifier = Modifier.padding(15.dp),
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.padding(30.dp))

            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        activity.startActivity(Intent(Settings.ACTION_BIOMETRIC_ENROLL));
                    } else{
                        Platform.biometricAuthenticate("TapStore Demo", "Desbloqueie o Dispositivo", activity)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(text = "Ir para as configurações do dispositivo", fontSize = 14.sp)
            }
        }
    }
}

