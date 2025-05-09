package com.example.diarytravel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.diarytravel.FragmentoMenuLateral
import com.example.diarytravel.PerfilActivity
import com.example.diarytravel.R
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Local
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CriarPontos : AppCompatActivity() {

    // Referências para os componentes da UI
    private lateinit var editTextTitulo: EditText
    private lateinit var editTextRegiao: EditText
    private lateinit var editTextComentario: EditText
    private lateinit var imageViewPaisagem: ImageView
    private lateinit var btnSelecionarFoto: MaterialButton
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private lateinit var imageButtonPerfil: ImageButton
    private lateinit var imageButtonMenu: ImageButton
    private var selectedImageUri: android.net.Uri? = null

    // Variáveis de imagem
    private var selectedImage: Bitmap? = null

    // Código de requisição para selecionar imagem
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageViewPaisagem.setImageURI(it)
            selectedImage = uriToBitmap(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_pontos)

        // Inicializar os componentes
        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextRegiao = findViewById(R.id.editTextRegiao)
        editTextComentario = findViewById(R.id.editTextComentario)
        imageViewPaisagem = findViewById(R.id.imageView4)
        btnSelecionarFoto = findViewById(R.id.btnSelecionarFoto)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)


        // Ação para o botão de selecionar foto
        btnSelecionarFoto.setOnClickListener {
            selectImageLauncher.launch("image/*")  // Abre a galeria de imagens
        }

        // Ação para o botão de adicionar ponto (guardar)
        btnGuardar.setOnClickListener {
            salvarPonto()
        }

        // Ação para o botão de cancelar
        btnCancelar.setOnClickListener {
            finish()  // Fecha a tela atual
        }



    }

    private fun salvarPonto() {
        val titulo = editTextTitulo.text.toString().trim()
        val regiao = editTextRegiao.text.toString().trim()
        val comentario = editTextComentario.text.toString().trim()

        if (titulo.isEmpty() || regiao.isEmpty() || comentario.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        // Pega o caminho da imagem (se houver)
        val caminhoFoto = selectedImageUri?.toString()

        val novoLocal = Local(
            titulo = titulo,
            regiao = regiao,
            comentario = comentario,
            caminhoFoto = caminhoFoto
        )

        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.LocalDao().inserirLocal(novoLocal)
            }

            runOnUiThread {
                Toast.makeText(this@CriarPontos, "Ponto criado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    // Função para converter URI de imagem para Bitmap
    private fun uriToBitmap(uri: android.net.Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }
    }
}
