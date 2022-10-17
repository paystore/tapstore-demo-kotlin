package br.com.phoebus.payments_demo.ui.components.menu

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.phoebus.payments.tef.BuildConfig
import br.com.phoebus.payments.ui.UI
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation
import br.com.phoebus.payments_demo.ui.theme.Color
import br.com.phoebus.payments_demo.utils.Identification
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun Customization(navController: NavController , context: Context) {

    val controller = rememberColorPickerController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlphaTile(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(6.dp)),
                controller = controller
            )
        }
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 15.dp),
            controller = controller,
            onColorChanged = {
                Color.primary = it.color
            }
        )
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            controller = controller,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                UI.finish()
                UI.init(context, lightColors(
                    primary = Color.primary,
                    primaryVariant = Color.primaryVariant,
                    onPrimary = Color.onPrimary,
                    secondary = Color.secondary,
                    secondaryVariant = Color.secondaryVariant,
                    onSecondary = Color.onSecondary,
                    surface = Color.surface,
                    onSurface = Color.onSurface,
                    error = Color.error,
                    background = Color.background,
                    onBackground = Color.onBackground,
                    onError = Color.onError
                ), Identification.basicRequest, BuildConfig.BUILD_TYPE)
                navController.navigate(Navigation.MainScreen.route)
            },
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.onPrimary
            )
        ) {
            Text("Set", fontSize = 15.sp)
        }
    }


}