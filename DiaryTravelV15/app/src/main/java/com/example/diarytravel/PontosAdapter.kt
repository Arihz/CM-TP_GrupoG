package com.example.diarytravel

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diarytravel.entities.Local
import com.example.diarytravel.R
import com.example.diarytravel.EditarPontos

class PontosAdapter(
    private val pontos: List<Local>,
    private val onEdit: (Local) -> Unit,
    private val onDelete: (Local) -> Unit,
    private val onView: (Local) -> Unit
) : RecyclerView.Adapter<PontosAdapter.PontosViewHolder>() {



    inner class PontosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.textTitulo)
        val regiao: TextView = view.findViewById(R.id.textlocalizacao)
        val btnVer: ImageButton = view.findViewById(R.id.imageVer)
        val btnEdit: ImageButton = view.findViewById(R.id.imageEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.imageDelete)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PontosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viagensview, parent, false)
        return PontosViewHolder(view)
    }



    override fun onBindViewHolder(holder: PontosViewHolder, position: Int) {
        val ponto = pontos[position]



        holder.titulo.text = ponto.titulo
        holder.regiao.text = ponto.regiao



        holder.btnVer.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, verPontos::class.java)
            intent.putExtra("Locais_id", ponto.id)
            context.startActivity(intent)
        }


        // Trecho corrigido da classe PontosAdapter
        holder.btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditarPontos::class.java)
            intent.putExtra("local_id", ponto.id)
            context.startActivity(intent)
        }


        holder.btnDelete.setOnClickListener {
            onDelete(ponto)
        }
    }


    override fun getItemCount(): Int = pontos.size
}
