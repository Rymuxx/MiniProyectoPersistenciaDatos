package com.example.miniproyectopersistenciadatos.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("config")

class Preferencias(private val context: Context) {
    private val KEY_NOMBRE = stringPreferencesKey("nombre_usuario")

    val nombreUsuario: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_NOMBRE] ?: "Desconocido" }

    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE] = nombre
        }
    }
}
