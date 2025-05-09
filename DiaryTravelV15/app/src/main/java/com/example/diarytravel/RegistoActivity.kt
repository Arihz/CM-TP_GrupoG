package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Utilizador
import com.example.diarytravel.repository.UtilizadorRepository
import kotlinx.coroutines.launch

class RegistoActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegistar: Button
    private lateinit var btnCancelar: Button

    private lateinit var utilizadorRepository: UtilizadorRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo)

        // Inicializar repositório
        val dao = AppDatabase.getDatabase(this).utilizadorDao()
        utilizadorRepository = UtilizadorRepository(dao)

        // Inicializar vistas
        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etRegistroEmail)
        etPassword = findViewById(R.id.etRegistroSenha)
        etConfirmPassword = findViewById(R.id.etConfirmarSenha)
        btnRegistar = findViewById(R.id.btnRegistrar)
        btnCancelar = findViewById(R.id.btnIrParaLogin)

        // Botão de registo
        btnRegistar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validarCampos(nome, email, password, confirmPassword)) {
                registarUtilizador(nome, email, password)
            }
        }

        // Botão para voltar ao login
        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun validarCampos(nome: String, email: String, password: String, confirmPassword: String): Boolean {
        if (nome.isEmpty()) {
            etNome.error = "O nome é obrigatório"
            return false
        }

        if (email.isEmpty()) {
            etEmail.error = "O email é obrigatório"
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "A palavra-passe é obrigatória"
            return false
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "As palavras-passe não coincidem"
            return false
        }

        return true
    }

    private fun registarUtilizador(nome: String, email: String, password: String) {
        lifecycleScope.launch {
            // Verificar se o email já existe
            val emailExiste = utilizadorRepository.verificarEmailExistente(email)

            if (emailExiste) {
                Toast.makeText(
                    this@RegistoActivity,
                    "Este email já está registado",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Criar novo utilizador
                val novoUtilizador = Utilizador(
                    nome = nome,
                    email = email,
                    password = password
                )

                val id = utilizadorRepository.registarUtilizador(novoUtilizador)

                if (id > 0) {
                    // Guardar ID do utilizador nas preferências
                    val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putLong("utilizador_id", id)
                        apply()
                    }

                    Toast.makeText(
                        this@RegistoActivity,
                        "Registo efetuado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Ir para a página principal
                    val intent = Intent(this@RegistoActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    Toast.makeText(
                        this@RegistoActivity,
                        "Erro ao efetuar o registo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}