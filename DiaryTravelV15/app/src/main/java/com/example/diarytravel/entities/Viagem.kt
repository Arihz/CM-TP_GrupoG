package com.example.diarytravel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "viagens")
data class Viagem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val data: Date,
    val classificacao: Float,
    val categoria: String,
    val localizacao: String,
    val comentario: String? = null,
    val caminhoFoto: String? = null
)
