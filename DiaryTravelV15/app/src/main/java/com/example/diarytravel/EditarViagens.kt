package com.example.diarytravel

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Viagem
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.text.SimpleDateFormat


class EditarViagens : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editData: EditText
    private lateinit var editClassificacao: EditText
    private lateinit var editCategoria: EditText
    private lateinit var editLocalizacao: EditText
    private lateinit var editComentario: EditText
    private lateinit var imageView: ImageView
    private var viagemId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_viagens)

        // Bind dos campos do layout
        editTitulo = findViewById(R.id.editTextTitulo)
        editData = findViewById(R.id.editTextData)
        editClassificacao = findViewById(R.id.editTextClassificacao)
        editCategoria = findViewById(R.id.editTextCategoria)
        editLocalizacao = findViewById(R.id.editTextLocalizacao)
        editComentario = findViewById(R.id.editTextComentario)
        imageView = findViewById(R.id.imageView4)

        // ID passado via Intent
        viagemId = intent.getLongExtra("viagem_id", -1)

        if (viagemId != -1L) {
            carregarDadosViagem(viagemId)
        }

        findViewById<MaterialButton>(R.id.btnAdicionar).setOnClickListener {
            atualizarViagem()
        }

        findViewById<MaterialButton>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btnMostrar).setOnClickListener {
            Toast.makeText(this, "Você já está a editar esta viagem.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarDadosViagem(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@EditarViagens)
            val viagem = db.ViagemDao().obterViagemPorId(id)

            viagem?.let {
                runOnUiThread {
                    editTitulo.setText(it.titulo)

                    // Converter o Date para String no formato desejado
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    editData.setText(sdf.format(it.data))  // Aqui a conversão de Date para String

                    editClassificacao.setText(it.classificacao.toString())
                    editCategoria.setText(it.categoria)
                    editLocalizacao.setText(it.localizacao)
                    editComentario.setText(it.comentario ?: "")
                    // imageView.setImageBitmap(...) se estiver a guardar fotos
                }
            }
        }
    }


    private fun atualizarViagem() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@EditarViagens)
            val viagem = db.ViagemDao().obterViagemPorId(viagemId)
            viagem?.let {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                // Converter o texto da editData de volta para Date
                val dataFormatada = try {
                    sdf.parse(editData.text.toString())
                } catch (e: Exception) {
                    null
                }

                // Se a data for inválida, mostrar um erro
                if (dataFormatada == null) {
                    runOnUiThread {
                        Toast.makeText(this@EditarViagens, "Data inválida", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Atualizar a viagem com os dados
                val viagemAtualizada = it.copy(
                    titulo = editTitulo.text.toString(),
                    data = dataFormatada,  // Agora você tem um Date válido
                    classificacao = editClassificacao.text.toString().toFloatOrNull() ?: 0f,
                    categoria = editCategoria.text.toString(),
                    localizacao = editLocalizacao.text.toString(),
                    comentario = editComentario.text.toString()
                )
                db.ViagemDao().atualizarViagem(viagemAtualizada)
                runOnUiThread {
                    Toast.makeText(this@EditarViagens, "Viagem atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

}
