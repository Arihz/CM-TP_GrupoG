package com.example.diarytravel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_perfil)

        // Inicializar campos
        editNome = findViewById(R.id.editTextText)
        editEmail = findViewById(R.id.editTextText2)
        editIdade = findViewById(R.id.editTextText3)
        editDescricao = findViewById(R.id.editTextText4)

        val db = AppDatabase.getDatabase(this)

        // Recuperar ID do utilizador logado
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        val userId = sharedPref.getLong("utilizador_id", -1L)

        if (userId == -1L) {
            Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Carregar dados do utilizador
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

        // Botão Guardar
        val btnGuardar = findViewById<MaterialButton>(R.id.btnIrParaLogin)
        btnGuardar.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val updatedUser = Utilizador(
                        id = userId,
                        nome = editNome.text.toString(),
                        email = editEmail.text.toString(),
                        password = "", // você pode buscar do DB se não for editável
                        idade = editIdade.text.toString().toIntOrNull(),
                        sobreMim = editDescricao.text.toString(),
                        caminhoFoto = null // implementar depois se necessário
                    )
                    db.utilizadorDao().atualizarUtilizador(updatedUser)
                }

                Toast.makeText(this@EditPerfil, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
