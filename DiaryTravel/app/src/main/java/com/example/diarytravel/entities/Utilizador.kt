package com.example.diarytravel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utilizadores")
data class Utilizador(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val email: String,
    val password: String,
    val idade: Int? = null,
    val sobreMim: String? = null,
    val caminhoFoto: String? = null
)