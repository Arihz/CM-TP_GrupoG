package com.example.diarytravel.dao

import androidx.room.*
import com.example.diarytravel.entities.Utilizador

@Dao
interface UtilizadorDao {
    @Insert
    suspend fun inserirUtilizador(utilizador: Utilizador): Long

    @Query("SELECT * FROM utilizadores WHERE email = :email AND password = :password LIMIT 1")
    suspend fun autenticarUtilizador(email: String, password: String): Utilizador?

    @Query("SELECT * FROM utilizadores WHERE id = :id")
    suspend fun obterUtilizadorPorId(id: Long): Utilizador?

    @Query("SELECT * FROM utilizadores WHERE email = :email")
    suspend fun verificarEmailExistente(email: String): Utilizador?

    @Update
    suspend fun atualizarUtilizador(utilizador: Utilizador)

    @Delete
    suspend fun removerUtilizador(utilizador: Utilizador)
}