package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diarytravel.db.AppDatabase
import com.example.diarytravel.entities.Local
import com.example.diarytravel.CriarPontos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Pontos : AppCompatActivity() {

    private var isMenuVisible = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PontosAdapter
    private val listaPontos = mutableListOf<Local>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pontos)  // ou outro layout, ex: activity_pontos

        val btnAddPonto = findViewById<Button>(R.id.btnAddPonto)
        btnAddPonto.setOnClickListener {
            val intent = Intent(this, CriarPontos::class.java)
            startActivity(intent)
        }
        // Menu lateral
        val btnToggleMenu = findViewById<ImageButton>(R.id.btnMenuLateral)
        val containerMenu = findViewById<View>(R.id.containerMenuLateral)

        btnToggleMenu.setOnClickListener {
            if (isMenuVisible) {
                supportFragmentManager.findFragmentById(R.id.containerMenuLateral)?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
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

        // Botão ir para perfil
        findViewById<ImageButton>(R.id.btnIrParaPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        // Configuração do RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPontos)
        adapter = PontosAdapter(
            pontos = listaPontos,
            onEdit = { ponto -> editarPonto(ponto) },
            onDelete = { ponto -> eliminarPonto(ponto) },
            onView = { ponto -> verDetalhes(ponto) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        carregarPontos()
    }

    override fun onResume() {
        super.onResume()
        carregarPontos()
    }

    private fun carregarPontos() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val pontos = withContext(Dispatchers.IO) {
                db.LocalDao().obterTodosLocais()
            }
            listaPontos.clear()
            listaPontos.addAll(pontos)
            adapter.notifyDataSetChanged()
        }
    }

    private fun editarPonto(ponto: Local) {
        val intent = Intent(this, EditarPontos::class.java)
        intent.putExtra("Locais_id", ponto.id)
        startActivity(intent)
    }

    private fun eliminarPonto(ponto: Local) {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.LocalDao().removerLocal(ponto)
            }
            carregarPontos()
        }
    }


    private fun verDetalhes(ponto: Local) {
        val intent = Intent(this, verPontos::class.java)
        intent.putExtra("Locais_id", ponto.id)
        startActivity(intent)
    }
}
