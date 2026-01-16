package com.example.miniproyectopersistenciadatos.Data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("config")

class Preferencias(private val context: Context) {
    // Definimos la clave
    private val KEY_NOMBRE = stringPreferencesKey("nombre_usuario")

    // Leer el nombre (devuelve un Flow)
    val nombreUsuario: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_NOMBRE] ?: "Desconocido" }

    // Guardar el nombre
    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE] = nombre
        }
    }
}