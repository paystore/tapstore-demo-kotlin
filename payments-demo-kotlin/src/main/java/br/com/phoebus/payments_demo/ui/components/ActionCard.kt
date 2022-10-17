package br.com.phoebus.payments_demo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.phoebus.payments_demo.ui.theme.Color

@Composable
fun ActionCard(icon: ImageVector, title: String, content: @Composable () -> Unit) {

    var visibilityState by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(top = 15.dp)
            .padding(horizontal = 15.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        border = BorderStroke(2.dp, Color.primary),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .clickable {
                        visibilityState = !visibilityState
                    }
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = Color.primary)
                Text(text = title)
                Icon(imageVector = Icons.Outlined.ExpandMore, contentDescription = "Expand")
            }
            AnimatedVisibility(visible = visibilityState,) {
                content()
            }
        }
    }
}