package br.com.phoebus.payments_demo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.phoebus.payments.ui.utils.Mask
import java.text.SimpleDateFormat
import br.com.phoebus.payments_demo.R

@Composable
fun Payment(
    brandName: String?,
    brandId: Long?,
    pan: String?,
    value: Long?,
    date: java.util.Date?,
    onSelected: () -> Unit?
) {

    val dateFormat = SimpleDateFormat("dd-MM-yyy HH:mm:ss")

    Surface(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = { onSelected() }
            )
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF7F7F7),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .wrapContentSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (brandName != null) {
                    when (brandName.lowercase()) {
                        "visa" -> Image(
                            painter = painterResource(id = R.drawable.ic_visa_logo),
                            contentDescription = brandName,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        "Master" -> Image(
                            painter = painterResource(id = R.drawable.ic_master_logo),
                            contentDescription = brandName,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        "Elo" -> Image(
                            painter = painterResource(id = R.drawable.ic_elo_logo),
                            contentDescription = brandName,
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    if (value != null) {
                        Text(
                            text = Mask.formatCurrency(value.toDouble() / 100),
                            fontSize = 20.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    if (pan != null) {
                        Text(
                            text = pan.drop(8)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    if (date != null) {
                        Text(text = dateFormat.format(date))
                    }
                }
            }
        }
    }
}