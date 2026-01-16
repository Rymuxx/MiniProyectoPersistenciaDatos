package com.example.miniproyectopersistenciadatos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichajes")
data class Fichaje(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val hora: String,
    val tipo: String // "ENTRADA" o "SALIDA"
)
