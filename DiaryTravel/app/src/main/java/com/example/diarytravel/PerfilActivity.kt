package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.repository.UtilizadorRepository
import kotlinx.coroutines.launch

class PerfilActivity : AppCompatActivity() {

    private lateinit var tvNomePerfil: TextView
    private lateinit var tvFrasePerfil: TextView
    private lateinit var btnEditarPerfil: Button
    private lateinit var btnRemoverConta: Button
    private lateinit var btnSair: Button

    private lateinit var utilizadorRepository: UtilizadorRepository
    private var utilizadorId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Obter ID do utilizador das preferências
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        utilizadorId = sharedPref.getLong("utilizador_id", 0)

        // Se não houver utilizador logado, voltar para login
        if (utilizadorId == 0L) {
            irParaLogin()
            return
        }

        // Inicializar repositório
        val dao = AppDatabase.getDatabase(this).utilizadorDao()
        utilizadorRepository = UtilizadorRepository(dao)

        // Inicializar vistas
        tvNomePerfil = findViewById(R.id.tvNomePerfil)
        tvFrasePerfil = findViewById(R.id.tvFrasePerfil)
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil)
        btnRemoverConta = findViewById(R.id.btnRemoverConta)
        btnSair = findViewById(R.id.btnSair)


        // Carregar dados do utilizador
        carregarDadosUtilizador()

        // Configurar botões
        btnSair.setOnClickListener {
            terminarSessao()
        }

        btnRemoverConta.setOnClickListener {
            confirmarRemoverConta()
        }

        btnEditarPerfil.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        val btn = findViewById<Button>(R.id.btnIrParaPerfil)
        btn.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun carregarDadosUtilizador() {
        lifecycleScope.launch {
            val utilizador = utilizadorRepository.obterUtilizadorPorId(utilizadorId)

            if (utilizador != null) {
                tvNomePerfil.text = utilizador.nome
                tvFrasePerfil.text = utilizador.sobreMim ?: "Utilizador DiaryTravel"
            } else {
                irParaLogin()
            }
        }
    }

    private fun terminarSessao() {
        // Limpar preferências
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("utilizador_id")
            apply()
        }

        irParaLogin()
    }

    private fun irParaLogin() {
        val intent = Intent(this, EntrarActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun confirmarRemoverConta() {
        AlertDialog.Builder(this)
            .setTitle("Remover Conta")
            .setMessage("Tem a certeza que pretende remover a sua conta? Esta ação não pode ser desfeita.")
            .setPositiveButton("Sim") { _, _ ->
                removerConta()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun removerConta() {
        lifecycleScope.launch {
            val utilizador = utilizadorRepository.obterUtilizadorPorId(utilizadorId)

            if (utilizador != null) {
                utilizadorRepository.removerUtilizador(utilizador)

                Toast.makeText(
                    this@PerfilActivity,
                    "A sua conta foi removida com sucesso",
                    Toast.LENGTH_SHORT
                ).show()

                terminarSessao()
            }
        }
    }
}