package com.example.diarytravel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Locais")
data class Local(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val regiao: String,
    val comentario: String? = null,
    val caminhoFoto: String? = null
)