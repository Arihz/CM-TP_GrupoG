package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Local
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarPontos : AppCompatActivity() {

    private var isMenuVisible = false
    private var localId: Long? = null

    private lateinit var editTitulo: EditText
    private lateinit var editRegiao: EditText
    private lateinit var editComentario: EditText
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_pontos)




        // Inicializar campos
        editTitulo = findViewById(R.id.editTextTitulo)
        editRegiao = findViewById(R.id.editTextRegiao)
        editComentario = findViewById(R.id.editTextComentario)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        val db = AppDatabase.getDatabase(this)
        val localDao = db.LocalDao()

        // Obter o ID passado via Intent
        val receivedId = intent.getLongExtra("local_id", -1)
        localId = if (receivedId != -1L) receivedId else null
        Log.d("EditarPontos", "localId recebido: $localId")

        if (localId == null) {
            Toast.makeText(this, "ID do local não fornecido.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Carregar dados se for edição
        lifecycleScope.launch {
            val local = withContext(Dispatchers.IO) {
                localDao.obterLocalPorId(localId!!)
            }

            if (local != null) {
                editTitulo.setText(local.titulo)
                editRegiao.setText(local.regiao)
                editComentario.setText(local.comentario)
                Log.d("EditarPontos", "Local carregado: $local")
            } else {
                Toast.makeText(this@EditarPontos, "Erro: Local não encontrado!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Guardar (atualizar ou inserir)
        btnGuardar.setOnClickListener {
            val titulo = editTitulo.text.toString().trim()
            val regiao = editRegiao.text.toString().trim()
            val comentario = editComentario.text.toString().trim()

            if (titulo.isBlank() || regiao.isBlank()) {
                Toast.makeText(this, "Preencha o título e a região!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val local = Local(
                        id = localId ?: 0,
                        titulo = titulo,
                        regiao = regiao,
                        comentario = comentario.ifBlank { null }
                    )

                    if (localId != null) {
                        localDao.atualizarLocal(local)
                    } else {
                        localDao.inserirLocal(local.copy(id = 0)) // ID gerado automaticamente
                    }
                }

                runOnUiThread {
                    Toast.makeText(this@EditarPontos, "Local guardado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        // Cancelar
        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
