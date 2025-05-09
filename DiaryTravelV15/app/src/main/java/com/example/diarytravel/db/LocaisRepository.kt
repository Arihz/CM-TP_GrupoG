package com.example.diarytravel.repository

import com.example.diarytravel.dao.LocalDao
import com.example.diarytravel.entities.Local

class LocaisRepository(private val lugarDao: LocalDao) {

    suspend fun inserirLocal(lugar: Local): Long {
        return lugarDao.inserirLocal(lugar)
    }

    suspend fun obterTodosLocal(): List<Local> {
        return lugarDao.obterTodosLocais()
    }

    suspend fun obterLocalPorId(id: Long): Local? {
        return lugarDao.obterLocalPorId(id)
    }

    suspend fun atualizarLocal(lugar: Local) {
        lugarDao.atualizarLocal(lugar)
    }

    suspend fun removerLocal(lugar: Local) {
        lugarDao.removerLocal(lugar)
    }
}
