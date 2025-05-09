package com.example.diarytravel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton

class FragmentoMenuLateral : Fragment(R.layout.menulateral) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botão Home
        view.findViewById<ImageButton>(R.id.imageButtonHome).setOnClickListener {
           startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        // Botão Travel
        view.findViewById<ImageButton>(R.id.imageButtonTravel).setOnClickListener {
            startActivity(Intent(requireContext(), Viagens::class.java))
        }

        // Botão Local
        view.findViewById<ImageButton>(R.id.imageButtonLocal).setOnClickListener {

        }
    }
}