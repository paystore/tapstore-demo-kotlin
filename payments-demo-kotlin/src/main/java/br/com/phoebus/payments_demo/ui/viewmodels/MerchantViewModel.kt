package br.com.phoebus.payments_demo.ui.viewmodels

import android.app.Application
import br.com.phoebus.payments.tef.Config
import br.com.phoebus.payments.tef.request.BasicRequest

class MerchantViewModel(application: Application
) : GenericViewModel(application) {

    private val basicRequest = BasicRequest(appIdentification, appCredentials)

    var merchantId: String? = null
    var terminalId: String? = null
    var networkId: String? = null
    var networkName: String? = null
    var nationalId: String? = null
    var merchantName: String? = null
    var merchantCommercialName: String? = null
    var merchantEMail: String? = null
    var merchantPhone: String? = null
    var merchantAddressCountryName: String? = null
    var merchantAddressPostalCode: String? = null
    var merchantAddressStreet: String? = null
    var merchantAddressStateAbbreviation: String? = null
    var merchantAddressNeighbourhood: String? = null
    var merchantAddressNumber: String? = null
    var merchantCategoryCode: String? = null
    var merchantAddressCityName: String? = null

    init {
        getItemsMerchantData()
    }

    fun getItemsMerchantData() {
        val identifications = Config.getPayStoreIdentifications(basicRequest).value

        merchantId = identifications?.get("merchantId")
        terminalId = identifications?.get("terminalId")
        networkId = identifications?.get("networkId")
        networkName = identifications?.get("networkName")
        nationalId = identifications?.get("nationalId")
        merchantName = identifications?.get("merchantName")
        merchantCommercialName = identifications?.get("merchantCommercialName")
        merchantEMail = identifications?.get("merchantEMail")
        merchantPhone = identifications?.get("merchantPhone")
        merchantAddressPostalCode = identifications?.get("merchantAddressPostalCode")
        merchantAddressStreet = identifications?.get("merchantAddressStreet")
        merchantAddressCountryName = identifications?.get("merchantAddressCountryName")
        merchantAddressStateAbbreviation = identifications?.get("merchantAddressStateAbbreviation")
        merchantAddressNeighbourhood = identifications?.get("merchantAddressNeighbourhood")
        merchantAddressNumber = identifications?.get("merchantAddressNumber")
        merchantCategoryCode = identifications?.get("merchantCategoryCode")
        merchantAddressCityName = identifications?.get("merchantAdressCity")
    }


}