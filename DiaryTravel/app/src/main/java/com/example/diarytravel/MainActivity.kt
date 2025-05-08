package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btnIrParaPerfil)
        btn.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        // Verificar se o utilizador está logado
        val sharedPref = getSharedPreferences("DiaryTravel", MODE_PRIVATE)
        val utilizadorId = sharedPref.getLong("utilizador_id", 0)

        // Redirecionar para a activity apropriada
        val intent = if (utilizadorId > 0) {
                Intent(this, MainActivity::class.java)
        } else {
            Intent(this, EntrarActivity::class.java)
        }



    }
}