package com.example.miniproyectopersistenciadatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.miniproyectopersistenciadatos.data.Preferencias
import com.example.miniproyectopersistenciadatos.database.AppDatabase
import com.example.miniproyectopersistenciadatos.ui.MainViewModel
import com.example.miniproyectopersistenciadatos.ui.screens.PantallaFichaje
import com.example.miniproyectopersistenciadatos.ui.theme.MiniProyectoPersistenciaDatosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val prefs = Preferencias(this)

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(db.fichajeDao(), prefs) as T
            }
        }

        val viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        setContent {
            MiniProyectoPersistenciaDatosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaFichaje(viewModel)
                }
            }
        }
    }
}

