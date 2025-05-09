package com.example.diarytravel.dao

import androidx.room.*
import com.example.diarytravel.entities.Local

@Dao
interface LocalDao {
    @Insert
    suspend fun inserirLocal(lugar: Local): Long

    @Query("SELECT * FROM Locais")
    suspend fun obterTodosLocais(): List<Local>

    @Query("SELECT * FROM Locais WHERE id = :id")
    suspend fun obterLocalPorId(id: Long): Local?

    @Update
    suspend fun atualizarLocal(lugar: Local)

    @Delete
    suspend fun removerLocal(lugar: Local)
}
