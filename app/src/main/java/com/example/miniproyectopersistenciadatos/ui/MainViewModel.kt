package com.example.miniproyectopersistenciadatos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproyectopersistenciadatos.data.Fichaje
import com.example.miniproyectopersistenciadatos.data.Preferencias
import com.example.miniproyectopersistenciadatos.database.FichajeDao
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
            val fechaHora =
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
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
