package com.simpletools.networkinspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Dashboard(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Dashboard(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("SimpleTools Network Inspector", style = MaterialTheme.typography.headlineMedium)
        Text("by AIWORKPAY — V1.0.0")
        Text("Dashboard — diagnostics modules under implementation")
    }
}
