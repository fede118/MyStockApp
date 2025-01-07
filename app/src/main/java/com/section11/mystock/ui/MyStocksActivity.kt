package com.section11.mystock.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.section11.mystock.MyStocksApplication
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyStocksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MyStocksApp()
        }
    }
}

