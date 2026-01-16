package com.example.miniproyectopersistenciadatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.miniproyectopersistenciadatos.Data.Preferencias
import com.example.miniproyectopersistenciadatos.Room.AppDatabase
import com.example.miniproyectopersistenciadatos.Room.Fichaje
import com.example.miniproyectopersistenciadatos.Room.FichajeDao
import com.example.miniproyectopersistenciadatos.ui.theme.MiniProyectoPersistenciaDatosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val dao: FichajeDao,
    private val prefs: Preferencias
) : ViewModel() {

    private val _nombreInput = MutableStateFlow("")
    val nombreInput: StateFlow<String> = _nombreInput.asStateFlow()

    val historialFichajes = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val nombreGuardado = prefs.nombreUsuario.first()
            _nombreInput.value = if (nombreGuardado == "Desconocido") "" else nombreGuardado
        }
    }

    fun actualizarNombre(nuevoNombre: String) {
        _nombreInput.value = nuevoNombre
        viewModelScope.launch {
            prefs.guardarNombre(nuevoNombre)
        }
    }

    fun registrarFichaje(tipo: String) {
        viewModelScope.launch {
            val fechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val nombreActual = _nombreInput.value.ifBlank { "Anónimo" }
            dao.registrar(Fichaje(nombre = nombreActual, hora = fechaHora, tipo = tipo))
        }
    }

    fun borrarHistorial() {
        viewModelScope.launch {
            dao.borrarTodo()
        }
    }
}

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

@Composable
fun PantallaFichaje(viewModel: MainViewModel) {
    val nombre by viewModel.nombreInput.collectAsState()
    val lista by viewModel.historialFichajes.collectAsState()

    PantallaFichajeContent(
        nombre = nombre,
        lista = lista,
        onNombreChange = { viewModel.actualizarNombre(it) },
        onFichajeClick = { tipo -> viewModel.registrarFichaje(tipo) },
        onBorrarTodoClick = { viewModel.borrarHistorial() }
    )
}

@Composable
fun PantallaFichajeContent(
    nombre: String,
    lista: List<Fichaje>,
    onNombreChange: (String) -> Unit,
    onFichajeClick: (String) -> Unit,
    onBorrarTodoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuración de Usuario",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Tu Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de Entrada y Salida
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onFichajeClick("ENTRADA") },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde
            ) {
                Text("ENTRADA")
            }

            Button(
                onClick = { onFichajeClick("SALIDA") },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Rojo
            ) {
                Text("SALIDA")
            }
        }

        TextButton(
            onClick = onBorrarTodoClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Borrar Historial", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Historial de Fichajes:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
        ) {
            items(lista) { fichaje ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (fichaje.tipo == "ENTRADA") 
                            Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = fichaje.nombre, style = MaterialTheme.typography.bodyLarge)
                            Text(text = fichaje.hora, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            text = fichaje.tipo,
                            color = if (fichaje.tipo == "ENTRADA") Color(0xFF2E7D32) else Color(0xFFC62828),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaFichajePreview() {
    MiniProyectoPersistenciaDatosTheme {
        PantallaFichajeContent(
            nombre = "Juan Pérez",
            lista = listOf(
                Fichaje(1, "Juan Pérez", "16/01/2026 10:30:00", "ENTRADA"),
                Fichaje(2, "Juan Pérez", "16/01/2026 14:45:00", "SALIDA")
            ),
            onNombreChange = {},
            onFichajeClick = {},
            onBorrarTodoClick = {}
        )
    }
}