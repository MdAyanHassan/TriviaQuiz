package com.example.triviaquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.triviaquiz.mainui.App
import com.example.triviaquiz.model.MediaManager
import com.example.triviaquiz.model.SharedPreferencesManager
import com.example.triviaquiz.ui.theme.TriviaQuizTheme

/**
 * MainActivity displays the main screen of our app
 */

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        SharedPreferencesManager.init(this)
        MediaManager.init(this)
        setContent {
            TriviaQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    App()
                }
            }
        }
    }
}
