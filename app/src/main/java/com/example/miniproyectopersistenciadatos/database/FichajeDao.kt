package com.example.miniproyectopersistenciadatos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.miniproyectopersistenciadatos.data.Fichaje
import kotlinx.coroutines.flow.Flow

@Dao
interface FichajeDao {
    @Insert
    suspend fun registrar(fichaje: Fichaje)

    @Query("SELECT * FROM fichajes ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Fichaje>>

    @Query("DELETE FROM fichajes")
    suspend fun borrarTodo()
}
