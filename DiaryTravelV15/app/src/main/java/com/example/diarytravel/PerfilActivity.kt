package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
    private var isMenuVisible = false
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

        // Inicializar as vistas
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
            val btn = findViewById<Button>(R.id.btnEditarPerfil)
            btn.setOnClickListener {
                val intent = Intent(this, EditPerfil::class.java)
                startActivity(intent)
            }
        }


        //criação das variaveis que controlam o fragmento do menu
        val btnToggleMenu = findViewById<ImageButton>(R.id.btnMenuLateral)
        val containerMenu = findViewById<View>(R.id.containerMenuLateral)

        //logica que permite abrir e fechar o menu
        btnToggleMenu.setOnClickListener {
            if (isMenuVisible) {
                supportFragmentManager.findFragmentById(R.id.containerMenuLateral)?.let {
                    supportFragmentManager.beginTransaction()
                        .remove(it)
                        .commit()
                }
                containerMenu.visibility = View.GONE
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerMenuLateral, FragmentoMenuLateral())
                    .commit()
                containerMenu.visibility = View.VISIBLE
            }
            isMenuVisible = !isMenuVisible
        }


    }

    private fun carregarDadosUtilizador() {
        lifecycleScope.launch {
            val utilizador = utilizadorRepository.obterUtilizadorPorId(utilizadorId)

            if (utilizador != null) {
                tvNomePerfil.text = utilizador.nome
                tvFrasePerfil.text = utilizador.sobreMim ?: getString(R.string.descricao_padrao_utilizador)
            } else {
                irParaLogin()
            }
        }
    }

    private fun terminarSessao() {

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
            .setTitle(getString(R.string.remover_conta))
            .setMessage(R.string.excluir_conta_pergunta)
            .setPositiveButton(getString(R.string.sim)) { _, _ ->
                removerConta()
            }
            .setNegativeButton(getString(R.string.nao), null)
            .show()
    }

    private fun removerConta() {
        lifecycleScope.launch {
            val utilizador = utilizadorRepository.obterUtilizadorPorId(utilizadorId)

            if (utilizador != null) {
                utilizadorRepository.removerUtilizador(utilizador)

                Toast.makeText(
                    this@PerfilActivity,
                    R.string.conta_removida_sucesso,
                    Toast.LENGTH_SHORT
                ).show()

                terminarSessao()
            }
        }
    }
}