package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var isMenuVisible = false  // Variável de controle do menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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


        // Botão Travel
        findViewById<ImageButton>(R.id.btnIrParaPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }


        // Verificar se o utiliza presente
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        val utilizadorId = sharedPref.getLong("utilizador_id", 0)

        if (utilizadorId == 0L) {
            startActivity(Intent(this, EntrarActivity::class.java))
            finish()
        }
    }
}
