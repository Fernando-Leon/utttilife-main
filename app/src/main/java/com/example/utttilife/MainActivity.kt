package com.example.utttilife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
import com.example.utttilife.screens.MainScreen
import com.example.utttilife.ui.theme.UtttilifeTheme
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.tracking.ActivityViewTrackingStrategy

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val clientToken = "puba31c298ff858c767b446aa6781add297"
        val environmentName = "staging-1"
        val appVariantName = "<APP_VARIANT_NAME>"
        val trackingConsent = TrackingConsent.GRANTED
        val durationThreshold = 0L // Define el umbral de duración

        // Usa una estrategia de seguimiento de vistas automática para actividades
        val strategy = ActivityViewTrackingStrategy(true)

        val configuration = Configuration.Builder(
            clientToken = clientToken,
            env = environmentName,
            variant = appVariantName
        )
            .useSite(DatadogSite.US5)
            .build()
        Datadog.initialize(this, configuration, trackingConsent)

        val applicationId = "8f694775-bb99-4bd7-b0ae-abef903c76eb"
        val rumConfiguration = RumConfiguration.Builder(applicationId)
            .trackUserInteractions()
            .trackLongTasks(durationThreshold)
            .useViewTrackingStrategy(strategy)
            .build()
        Rum.enable(rumConfiguration)
        setContent {
            UtttilifeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   // LoginScreen()
                 MainScreen()
                    //suka
                }
            }
        }
    }
}

