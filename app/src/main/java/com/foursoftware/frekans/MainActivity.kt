package com.foursoftware.frekans
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.foursoftware.frekans.navigation.NavGraph
import com.foursoftware.frekans.ui.theme.FrekansTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val attributionContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                createAttributionContext("audioPlayback")
            } catch (e: Exception) {
                this
            }
        } else {
            this
        }
        enableEdgeToEdge()
        setContent {
            FrekansTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}