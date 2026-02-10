package com.example.miniproyectopersistenciadatos.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miniproyectopersistenciadatos.data.Fichaje
import com.example.miniproyectopersistenciadatos.ui.MainViewModel
import com.example.miniproyectopersistenciadatos.ui.theme.MiniProyectoPersistenciaDatosTheme

@Composable
fun PantallaFichaje(viewModel: MainViewModel = viewModel()) {
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onFichajeClick("ENTRADA") },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ENTRADA")
            }

            Button(
                onClick = { onFichajeClick("SALIDA") },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
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
                            color = if (fichaje.tipo == "ENTRADA") Color(0xFF2E7D32) else Color(
                                0xFFC62828
                            ),
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
