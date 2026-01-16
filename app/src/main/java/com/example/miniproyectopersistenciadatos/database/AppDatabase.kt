package com.example.miniproyectopersistenciadatos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miniproyectopersistenciadatos.data.Fichaje

@Database(entities = [Fichaje::class], version = 3)
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
