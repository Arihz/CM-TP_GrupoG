package com.example.diarytravel.repository

import com.example.diarytravel.dao.UtilizadorDao
import com.example.diarytravel.entities.Utilizador

class UtilizadorRepository(private val utilizadorDao: UtilizadorDao) {
    suspend fun registarUtilizador(utilizador: Utilizador): Long {
        return utilizadorDao.inserirUtilizador(utilizador)
    }

    suspend fun autenticarUtilizador(email: String, password: String): Utilizador? {
        return utilizadorDao.autenticarUtilizador(email, password)
    }

    suspend fun obterUtilizadorPorId(id: Long): Utilizador? {
        return utilizadorDao.obterUtilizadorPorId(id)
    }

    suspend fun verificarEmailExistente(email: String): Boolean {
        return utilizadorDao.verificarEmailExistente(email) != null
    }

    suspend fun atualizarUtilizador(utilizador: Utilizador) {
        utilizadorDao.atualizarUtilizador(utilizador)
    }

    suspend fun removerUtilizador(utilizador: Utilizador) {
        utilizadorDao.removerUtilizador(utilizador)
    }
}