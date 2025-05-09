package com.example.diarytravel.dao

import androidx.room.*
import com.example.diarytravel.entities.Viagem

@Dao
interface ViagemDao {

    @Insert
    suspend fun inserirViagem(viagem: Viagem): Long

    @Query("SELECT * FROM viagens WHERE id = :id")
    suspend fun obterViagemPorId(id: Long): Viagem?

    @Query("SELECT * FROM viagens")
    suspend fun obterTodasViagens(): List<Viagem>

    @Update
    suspend fun atualizarViagem(viagem: Viagem)

    @Delete
    suspend fun removerViagem(viagem: Viagem)
}
