package com.example.ari67

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ari67.presentation.BleScreen
import com.example.ari67.ui.theme.Ari67Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ari67Theme {
                BleScreen()
            }
        }
    }
}