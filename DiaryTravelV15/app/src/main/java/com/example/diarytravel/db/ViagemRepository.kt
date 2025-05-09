package com.example.diarytravel.repository

import com.example.diarytravel.dao.ViagemDao
import com.example.diarytravel.entities.Viagem

class ViagemRepository(private val viagemDao: ViagemDao) {

    suspend fun inserirViagem(viagem: Viagem): Long {
        return viagemDao.inserirViagem(viagem)
    }

    suspend fun obterViagemPorId(id: Long): Viagem? {
        return viagemDao.obterViagemPorId(id)
    }

    suspend fun obterTodasViagens(): List<Viagem> {
        return viagemDao.obterTodasViagens()
    }

    suspend fun atualizarViagem(viagem: Viagem) {
        viagemDao.atualizarViagem(viagem)
    }

    suspend fun removerViagem(viagem: Viagem) {
        viagemDao.removerViagem(viagem)
    }
}
