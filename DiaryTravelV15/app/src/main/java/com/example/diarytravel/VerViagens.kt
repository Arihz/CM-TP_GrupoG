package com.example.diarytravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diarytravel.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class VerViagens : AppCompatActivity() {

    private var isMenuVisible = false
    private var viagemId: Long = -1

    // Views da UI
    private lateinit var textTitulo: TextView
    private lateinit var textLocal: TextView
    private lateinit var textAvaliacao: TextView
    private lateinit var textComentario: TextView
    private lateinit var textData: TextView
    private lateinit var textPontos: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_viagens)

        // Layout edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa views
        textTitulo = findViewById(R.id.textTitulo)
        textLocal = findViewById(R.id.textLocal)
        textAvaliacao = findViewById(R.id.textAvaliacao)
        textComentario = findViewById(R.id.textComentario)
        textData = findViewById(R.id.textData)
        textPontos = findViewById(R.id.textPontos)
        imageView = findViewById(R.id.imageView3)

        // Menu lateral
        val btnToggleMenu = findViewById<ImageButton>(R.id.btnMenuLateral)
        val containerMenu = findViewById<View>(R.id.containerMenuLateral)

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

        // Pega ID da viagem da Intent
        viagemId = intent.getLongExtra("viagem_id", -1)
        if (viagemId != -1L) {
            carregarViagem()
        }
        val btnVoltar = findViewById<View>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarViagem() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@VerViagens)
            val viagem = db.ViagemDao().obterViagemPorId(viagemId)

            viagem?.let {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataFormatada = dateFormat.format(it.data)

                runOnUiThread {
                    textTitulo.text = "Título: ${it.titulo}"
                    textLocal.text = "Local: ${it.localizacao}"
                    textAvaliacao.text = "Avaliação: ${it.classificacao}"
                    textComentario.text = "Comentário: ${it.comentario ?: "Sem comentário"}"
                    textData.text = dataFormatada
                    textPontos.text = "Data:"

                    if (!it.caminhoFoto.isNullOrEmpty()) {
                        val uri = Uri.parse(it.caminhoFoto)
                        imageView.setImageURI(uri)
                    } else {
                        imageView.setImageResource(R.drawable.paisagem) // imagem padrão caso não haja foto
                    }
                }
            }
        }
    }

}
