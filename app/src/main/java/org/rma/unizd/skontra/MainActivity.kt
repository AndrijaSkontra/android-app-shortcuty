package org.rma.unizd.skontra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.rma.unizd.skontra.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Creating MainActivity")
        this.enableEdgeToEdge()
        this.setContent {
            HomeScreen()
        }
    }
}

