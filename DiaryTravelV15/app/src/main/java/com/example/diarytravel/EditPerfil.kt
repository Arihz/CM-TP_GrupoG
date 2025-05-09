package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Utilizador
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPerfil : AppCompatActivity() {

    private lateinit var editNome: EditText
    private lateinit var editEmail: EditText
    private lateinit var editIdade: EditText
    private lateinit var editDescricao: EditText
    private lateinit var db: AppDatabase
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_perfil)

        // Initialize fields
        editNome = findViewById(R.id.editTextText)
        editEmail = findViewById(R.id.editTextText2)
        editIdade = findViewById(R.id.editTextText3)
        editDescricao = findViewById(R.id.editTextText4)
        db = AppDatabase.getDatabase(this)

        // Get logged in user ID
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        userId = sharedPref.getLong("utilizador_id", -1L)

        if (userId == -1L) {
            Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load user data
        loadUserData()

        // Save button
        val btnGuardar = findViewById<MaterialButton>(R.id.btnParaGuardar)
        btnGuardar.setOnClickListener {
            saveUserData()
        }

        // Cancel button
        findViewById<MaterialButton>(R.id.btnCancelar).setOnClickListener {
            finish()
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val utilizador = withContext(Dispatchers.IO) {
                db.utilizadorDao().obterUtilizadorPorId(userId)
            }

            utilizador?.let {
                editNome.setText(it.nome)
                editEmail.setText(it.email)
                editIdade.setText(it.idade?.toString() ?: "")
                editDescricao.setText(it.sobreMim ?: "")
            }
        }
    }

    private fun saveUserData() {
        lifecycleScope.launch {
            val utilizador = withContext(Dispatchers.IO) {
                db.utilizadorDao().obterUtilizadorPorId(userId)
            }

            utilizador?.let {
                val updatedUser = Utilizador(
                    id = userId,
                    nome = editNome.text.toString(),
                    email = editEmail.text.toString(),
                    password = it.password, // Use the original password from the database
                    idade = editIdade.text.toString().toIntOrNull(),
                    sobreMim = editDescricao.text.toString(),
                    caminhoFoto = it.caminhoFoto
                )

                withContext(Dispatchers.IO) {
                    db.utilizadorDao().atualizarUtilizador(updatedUser)
                }

                Toast.makeText(this@EditPerfil, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@EditPerfil, PerfilActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
