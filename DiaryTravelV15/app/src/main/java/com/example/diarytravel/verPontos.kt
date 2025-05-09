package com.example.diarytravel

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

class verPontos : AppCompatActivity() {

    private var isMenuVisible = false
    private var localId: Long = -1

    // Views
    private lateinit var textTitulo: TextView
    private lateinit var textRegiao: TextView
    private lateinit var textComentario: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_pontos)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        textTitulo = findViewById(R.id.textTitulo)
        textRegiao = findViewById(R.id.textLocal)
        textComentario = findViewById(R.id.textComentario)
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

        // Pega o ID do Local
        localId = intent.getLongExtra("Locais_id", -1)
        if (localId != -1L) {
            carregarLocal()
        }

        // Botão voltar
        val btnVoltar = findViewById<View>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarLocal() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@verPontos)
            val local = db.LocalDao().obterLocalPorId(localId)

            local?.let {
                runOnUiThread {
                    textTitulo.text = "Título: ${it.titulo}"
                    textRegiao.text = "Região: ${it.regiao}"
                    textComentario.text = "Comentário: ${it.comentario ?: "Sem comentário"}"

                    // if (it.caminhoFoto != null) {
                    //     val bitmap = BitmapFactory.decodeFile(it.caminhoFoto)
                    //     imageView.setImageBitmap(bitmap)
                    // }
                }
            }
        }
    }
}
