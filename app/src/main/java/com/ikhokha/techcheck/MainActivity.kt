package com.ikhokha.techcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ikhokha.techcheck.ui.BusyShopApp
import com.ikhokha.techcheck.ui.theme.IKhokhaTechCheckTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IKhokhaTechCheckTheme {
                BusyShopApp()
            }
        }
    }
}
