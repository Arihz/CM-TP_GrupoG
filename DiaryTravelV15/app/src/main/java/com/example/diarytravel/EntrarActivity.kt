package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.repository.UtilizadorRepository
import kotlinx.coroutines.launch

class EntrarActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnEntrar: Button
    private lateinit var tvRegistar: TextView

    private lateinit var utilizadorRepository: UtilizadorRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar se já existe um utilizador logado
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        val utilizadorId = sharedPref.getLong("utilizador_id", 0)

        // Se já estiver logado, ir diretamente para o perfil
        if (utilizadorId > 0) {
            irParaMain()
            return
        }

        setContentView(R.layout.activity_entrar)

        // Inicializar repositório
        val dao = AppDatabase.getDatabase(this).utilizadorDao()
        utilizadorRepository = UtilizadorRepository(dao)

        // Inicializar vistas
        etEmail = findViewById(R.id.etLoginEmail)
        etPassword = findViewById(R.id.etLoginSenha)
        btnEntrar = findViewById(R.id.btnEntrar)
        tvRegistar = findViewById(R.id.tvIrParaRegistro)

        // Botão de login
        btnEntrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validarCampos(email, password)) {
                fazerLogin(email, password)
            }
        }

        // Ir para registo
        tvRegistar.setOnClickListener {
            val intent = Intent(this, RegistoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validarCampos(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            etEmail.error = "O email é obrigatório"
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "A palavra-passe é obrigatória"
            return false
        }

        return true
    }

    private fun fazerLogin(email: String, password: String) {
        lifecycleScope.launch {
            val utilizador = utilizadorRepository.autenticarUtilizador(email, password)

            if (utilizador != null) {
                // Guardar ID do utilizador nas preferências
                val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putLong("utilizador_id", utilizador.id)
                    apply()
                }

                // Ir para a página principal
                irParaMain()
            } else {
                Toast.makeText(
                    this@EntrarActivity,
                    "Email ou palavra-passe incorretos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun irParaMain() {
        val intent = Intent(this@EntrarActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}