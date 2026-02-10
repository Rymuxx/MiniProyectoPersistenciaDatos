package com.example.miniproyectopersistenciadatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.miniproyectopersistenciadatos.ui.screens.PantallaFichaje
import com.example.miniproyectopersistenciadatos.ui.theme.MiniProyectoPersistenciaDatosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniProyectoPersistenciaDatosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaFichaje()
                }
            }
        }
    }
}
