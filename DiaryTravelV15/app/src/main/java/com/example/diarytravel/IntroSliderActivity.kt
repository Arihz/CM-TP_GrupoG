package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class IntroSliderActivity : AppCompatActivity() {

    // Array com os layouts dos slides da intro
    private val slides = intArrayOf(
        R.layout.slide1,  // Primeiro slide
        R.layout.slide2   // Segundo slide
    )

    // Metodo chamado ao criar a atividade
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se o utilizador já viu a introdução
        if (getSharedPreferences("app", MODE_PRIVATE).getBoolean("intro_visto", false)) {
            // Se já viu, vai direto para a ecrã de login (EntrarActivity)
            startActivity(Intent(this, EntrarActivity::class.java))
            finish()  // Finaliza a ecrã de introdução para não voltar a ela
            return
        }

        setContentView(R.layout.activity_intro_slider)

        // Obtém referências para o ViewPager e os botões
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val btnPular = findViewById<Button>(R.id.btnPular)
        val botaoProximo = findViewById<Button>(R.id.botaoProximo)

        // Define o adaptador do ViewPager com os slides
        viewPager.adapter = object : PagerAdapter() {
            // Retorna o número total de slides
            override fun getCount() = slides.size

            // Verifica se a View corresponde ao objeto que é retornado pelo instantiateItem
            override fun isViewFromObject(view: View, obj: Any) = view == obj

            // Cria e adiciona a view do slide atual ao container
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = LayoutInflater.from(this@IntroSliderActivity)
                    .inflate(slides[position], container, false)
                container.addView(view)  // Adiciona o slide ao ViewPager
                return view  // Retorna a view adicionada
            }

            // Remove a view do slide quando não é mais necessária
            override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
                container.removeView(obj as View)  // Remove a view do ViewPager
            }
        }

        // Vai direto para o ecrã de entrar na conta
        btnPular.setOnClickListener { navegarEntrar() }

        // Passa para o proximo slide
        botaoProximo.setOnClickListener {
            if (viewPager.currentItem < slides.size - 1) {
                viewPager.currentItem += 1  // Vai para o próximo slide
            } else {
                navegarEntrar()  // Se estiver no último slide, vai para o ecrã de entrar na conta
            }
        }
    }

    // Função que salva no SharedPreferences que a introdução foi vista e navega para a próxima ecrã
    private fun navegarEntrar() {
        getSharedPreferences("app", MODE_PRIVATE)
            .edit()
            .putBoolean("intro_visto", true)  // Marca que o utilizador viu a introdução
            .apply()

        startActivity(Intent(this, EntrarActivity::class.java))  // Vai para a ecrã de login
        finish()  // Finaliza a atividade atual
    }
}