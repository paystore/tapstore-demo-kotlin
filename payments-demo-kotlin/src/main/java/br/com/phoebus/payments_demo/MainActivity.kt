package br.com.phoebus.payments_demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.phoebus.payments.core.prototype.Platform
import br.com.phoebus.payments.core.util.BiometricStatus
import br.com.phoebus.payments.core.util.BiometricUtil
import br.com.phoebus.payments.mobile.presentation.viewmodels.SystemViewModel
import br.com.phoebus.payments.tef.request.AppCredentials
import br.com.phoebus.payments.tef.request.AppIdentification
import br.com.phoebus.payments.tef.request.BasicRequest
import br.com.phoebus.payments_demo.ui.presentation.navigation.Navigation
import br.com.phoebus.payments_demo.ui.presentation.screens.LoginScreen
import br.com.phoebus.payments_demo.ui.presentation.screens.MainScreen
import br.com.phoebus.payments_demo.ui.presentation.screens.SplashScreen
import br.com.phoebus.payments_demo.ui.theme.MainTheme
import br.com.phoebus.payments_demo.utils.Identification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : FragmentActivity() {

    private val permissionsMap = mapOf(
        Manifest.permission.READ_PHONE_STATE to 1,
        Manifest.permission.ACCESS_FINE_LOCATION to 2,
        Manifest.permission.NFC to 3
    )

    private var systemViewModel: SystemViewModel? = null
    private var navController: NavHostController? = null

    private fun getApplicationIdentifier(context: Context): AppIdentification {
        return AppIdentification(
            "TapStore Demo",
            BuildConfig.APPLICATION_ID,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )
    }

    private fun getApplicationCredentials(): AppCredentials {
        return AppCredentials("", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Identification.appIdentification = getApplicationIdentifier(applicationContext)
        Identification.appCredentials = getApplicationCredentials()
        Identification.basicRequest =
            BasicRequest(Identification.appIdentification, Identification.appCredentials)

        if (Platform.biometricIsEnabled()) {
            Platform.setAuthenticated(false)
            Platform.biometricInstall(
                this,
                ::authenticationSucceeded,
                ::authenticationFailed,
                ::authenticationError
            )
        }

        setContent {
            mainContent()
        }
    }

    fun authenticationSucceeded() {
        Platform.setAuthenticated(true)
        navController!!.navigate(Navigation.MainScreen.route)
    }

    fun authenticationFailed() {
        Platform.setAuthenticated(false)
    }

    fun authenticationError() {
        Platform.setAuthenticated(false)
        navController!!.navigate(Navigation.Splash.route)
    }

    override fun onDestroy() {
        super.onDestroy()
        systemViewModel!!.systemFinish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            BiometricUtil.LOCK_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                authenticationSucceeded()
            } else {
                authenticationError()
            }
            BiometricUtil.SECURITY_SETTING_REQUEST_CODE ->
                if (Platform.biometricStatus(this@MainActivity.applicationContext) == BiometricStatus.BIOMETRIC_SUCCESS) {
                    Platform.biometricAuthenticate(
                        "TapStore Demo",
                        "Desbloqueie o seu dispositivo",
                        this@MainActivity
                    )
                } else {
                    //Cancelamento
                }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun mainContent() {
        systemViewModel = viewModel()
        navController = rememberNavController()
        MainTheme {
            Surface(color = MaterialTheme.colors.background) {
                NavHost(
                    navController = navController!!,
                    startDestination = Navigation.Splash.route
                ) {
                    composable(Navigation.Splash.route) {
                        SplashScreen {
                            if (checkForRequiredPermissions()) {
                                mainDecision()
                            }
                        }
                    }
                    composable(Navigation.MainScreen.route) {
                        MainScreen(navController!!, applicationContext)
                    }
                    composable(Navigation.LoginScreen.route) {
                        LoginScreen(navController!!, applicationContext)
                    }
                }
            }
        }
    }

    private fun mainDecision() {
        systemViewModel!!.systemInit(applicationContext,
            Identification.basicRequest,
            readyStatus = {
                navController!!.navigate(Navigation.MainScreen.route)
            },
            installStatus = {
                navController!!.navigate(Navigation.LoginScreen.route)
            },
            fail = { code, message ->
                Toast.makeText(
                    applicationContext,
                    code + message,
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
        )
    }

    private fun checkForRequiredPermissions(): Boolean {
        var result = true

        for (permission in permissionsMap) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission.key
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                result = false

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission.key),
                    permission.value
                )
            }
        }

        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty()) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (checkForRequiredPermissions()) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            //mainDecision()
                        }
                    }
                }
            } else {
                finish()
            }
        }
    }

}