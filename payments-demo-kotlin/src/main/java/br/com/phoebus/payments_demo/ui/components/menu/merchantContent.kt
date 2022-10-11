package br.com.phoebus.payments_demo.ui.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.phoebus.payments_demo.ui.viewmodels.MerchantViewModel

@Composable
fun merchantContent() {

    val merchantViewModel = viewModel<MerchantViewModel>()

    Column(
        modifier = Modifier
            .height(300.dp)
            .verticalScroll(rememberScrollState())
            .padding(15.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        info(title = "Código do estabelecimento", data = merchantViewModel.merchantId )
        info(title = "Código do Terminal Logico", data = merchantViewModel.terminalId )
        info(title = "ID Facilitator", data = merchantViewModel.networkId )
        info(title = "Nome do Facilitator", data = merchantViewModel.networkName )
        info(title = "ID National", data = merchantViewModel.nationalId )
        info(title = "Nome Fantasia", data = merchantViewModel.merchantName )
        info(title = "Nome do estabelecimento", data = merchantViewModel.merchantCommercialName )
        info(title = "Email", data = merchantViewModel.merchantEMail )
        info(title = "Telefone", data = merchantViewModel.merchantPhone )
        info(title = "CEP", data = merchantViewModel.merchantAddressPostalCode )
        info(title = "Rua", data = merchantViewModel.merchantAddressStreet )
        info(title = "Pais", data = merchantViewModel.merchantAddressCountryName )
        info(title = "Estado", data = merchantViewModel.merchantAddressStateAbbreviation )
        info(title = "Cidade", data = merchantViewModel.merchantAddressCityName )
        info(title = "Bairro", data = merchantViewModel.merchantAddressNeighbourhood )
        info(title = "Numero", data = merchantViewModel.merchantAddressNumber )
    }
}

@Composable
private fun info(title: String?, data: String?) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    ) {
        if (title != null) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
        }
        if (data != null) {
            Text(text = data, fontWeight = FontWeight.Light)
        }
        Divider(modifier = Modifier.padding(horizontal = 10.dp))
    }
}