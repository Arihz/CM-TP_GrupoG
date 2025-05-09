package com.example.diarytravel.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.diarytravel.dao.LocalDao
import com.example.diarytravel.dao.UtilizadorDao
import com.example.diarytravel.dao.ViagemDao
import com.example.diarytravel.entities.Local
import com.example.diarytravel.entities.Utilizador
import com.example.diarytravel.entities.Viagem
import com.example.diarytravel.util.Converters

@Database(entities = [Utilizador::class, Viagem::class, Local::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun utilizadorDao(): UtilizadorDao
    abstract fun ViagemDao(): ViagemDao
    abstract fun LocalDao(): LocalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
