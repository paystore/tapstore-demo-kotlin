package br.com.phoebus.payments_demo.utils

import br.com.phoebus.payments.tef.request.AppCredentials
import br.com.phoebus.payments.tef.request.AppIdentification
import br.com.phoebus.payments.tef.request.BasicRequest

object Identification {
    lateinit var appIdentification : AppIdentification
    lateinit var appCredentials: AppCredentials
    lateinit var basicRequest: BasicRequest
}