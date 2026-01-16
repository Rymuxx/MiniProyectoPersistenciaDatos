package com.example.miniproyectopersistenciadatos.Room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// A. Entidad (Tabla relacional)
@Entity(tableName = "fichajes")
data class Fichaje(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val hora: String,
    val tipo: String // "ENTRADA" o "SALIDA"
)

// B. DAO (Consultas SQL)
@Dao
interface FichajeDao {
    @Insert
    suspend fun registrar(fichaje: Fichaje)

    @Query("SELECT * FROM fichajes ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Fichaje>>

    @Query("DELETE FROM fichajes")
    suspend fun borrarTodo()
}

// C. Base de Datos (Singleton)
@Database(entities = [Fichaje::class], version = 3) // Incrementamos versión por el nuevo campo 'tipo'
abstract class AppDatabase : RoomDatabase() {
    abstract fun fichajeDao(): FichajeDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "fichaje_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}