package com.example.diarytravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Viagem
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CriarViagens : AppCompatActivity() {


    private lateinit var editTitulo: EditText
    private lateinit var editData: EditText
    private lateinit var editClassificacao: EditText
    private lateinit var editCategoria: EditText
    private lateinit var editLocalizacao: EditText
    private lateinit var editComentario: EditText
    private lateinit var btnAdicionar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private lateinit var imageViewFoto: ImageView
    private lateinit var btnSelecionarFoto: MaterialButton

    private var caminhoImagem: String? = null

    // Selecionar imagem da galeria
    private val selecionarImagem = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageViewFoto.setImageURI(uri)
            caminhoImagem = uri.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_viagens)



        // Inicialização das views
        editTitulo = findViewById(R.id.editTextTitulo)
        editData = findViewById(R.id.editTextData)
        editClassificacao = findViewById(R.id.editTextClassificacao)
        editCategoria = findViewById(R.id.editTextCategoria)
        editLocalizacao = findViewById(R.id.editTextLocalizacao)
        editComentario = findViewById(R.id.editTextComentario)
        btnAdicionar = findViewById(R.id.btnAdicionar)
        btnCancelar = findViewById(R.id.btnCancelar)
        imageViewFoto = findViewById(R.id.imageViewFoto)
        btnSelecionarFoto = findViewById(R.id.btnSelecionarFoto)

        btnSelecionarFoto.setOnClickListener {
            selecionarImagem.launch("image/*")
        }

        val db = AppDatabase.getDatabase(this)

        btnAdicionar.setOnClickListener {
            val titulo = editTitulo.text.toString()
            val dataTexto = editData.text.toString()
            val classificacao = editClassificacao.text.toString().toFloatOrNull()
            val categoria = editCategoria.text.toString()
            val localizacao = editLocalizacao.text.toString()
            val comentario = editComentario.text.toString()

            if (titulo.isBlank() || dataTexto.isBlank() || classificacao == null || categoria.isBlank() || localizacao.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val data: Date? = try {
                formato.parse(dataTexto)
            } catch (e: Exception) {
                null
            }

            if (data == null) {
                Toast.makeText(this, "Data inválida. Use o formato dd/MM/yyyy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novaViagem = Viagem(
                titulo = titulo,
                data = data,
                classificacao = classificacao,
                categoria = categoria,
                localizacao = localizacao,
                comentario = comentario.ifBlank { null },
                caminhoFoto = caminhoImagem
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db.ViagemDao().inserirViagem(novaViagem)
                }
                Toast.makeText(this@CriarViagens, "Viagem adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
