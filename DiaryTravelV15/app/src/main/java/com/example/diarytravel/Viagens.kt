package com.example.diarytravel

import ViagemAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Viagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Viagens : AppCompatActivity() {

    private var isMenuVisible = false  // Variável de controle do menu
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViagemAdapter
    private val listaViagens = mutableListOf<Viagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_viagens)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //criação das variaveis que controlam o fragmento do menu
        val btnToggleMenu = findViewById<ImageButton>(R.id.btnMenuLateral)
        val containerMenu = findViewById<View>(R.id.containerMenuLateral)

        //logica que permite abrir e fechar o menu
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
        val btn = findViewById<Button>(R.id.btnAddViagem)
        btn.setOnClickListener {
            val intent = Intent(this, CriarViagens::class.java)
            startActivity(intent)
        }

        // Botão Travel
        findViewById<ImageButton>(R.id.btnIrParaPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        recyclerView = findViewById(R.id.recyclerViewViagens)
        adapter = ViagemAdapter(listaViagens,
            onEdit = { viagem -> editarViagem(viagem) },
            onDelete = { viagem -> eliminarViagem(viagem) },
            onView = { viagem -> verDetalhes(viagem) }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        carregarViagens()
    }

    override fun onResume() {
        super.onResume()
        carregarViagens()
    }

    private fun carregarViagens() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val viagens = withContext(Dispatchers.IO) {
                db.ViagemDao().obterTodasViagens()
            }
            listaViagens.clear()
            listaViagens.addAll(viagens)
            adapter.notifyDataSetChanged()
        }
    }

    private fun editarViagem(viagem: Viagem) {
        // lógica para editar
    }

    private fun eliminarViagem(viagem: Viagem) {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.ViagemDao().removerViagem(viagem)
            }
            carregarViagens()
        }
    }

    private fun verDetalhes(viagem: Viagem) {
        // lógica para ver
    }
}