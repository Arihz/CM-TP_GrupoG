import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diarytravel.EditarViagens
import com.example.diarytravel.R
import com.example.diarytravel.VerViagens
import com.example.diarytravel.entities.Viagem

class ViagemAdapter(
    private val viagens: List<Viagem>,
    private val onEdit: (Viagem) -> Unit,
    private val onDelete: (Viagem) -> Unit,
    private val onView: (Viagem) -> Unit
) : RecyclerView.Adapter<ViagemAdapter.ViagemViewHolder>() {

    inner class ViagemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.textTitulo)
        val localizacao: TextView = view.findViewById(R.id.textlocalizacao)
        val imagem: ImageView = view.findViewById(R.id.imageViewViagem)
        val btnVer: ImageButton = view.findViewById(R.id.imageVer)
        val btnEdit: ImageButton = view.findViewById(R.id.imageEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.imageDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViagemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viagensview, parent, false)
        return ViagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViagemViewHolder, position: Int) {
        val viagem = viagens[position]
        holder.titulo.text = viagem.titulo
        holder.localizacao.text = viagem.localizacao

        // Carrega imagem com Glide (se existir)
        if (!viagem.caminhoFoto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Uri.parse(viagem.caminhoFoto))
                .placeholder(R.drawable.paisagem)
                .into(holder.imagem)
        } else {
            holder.imagem.setImageResource(R.drawable.paisagem)
        }

        holder.btnVer.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, VerViagens::class.java)
            intent.putExtra("viagem_id", viagem.id)
            context.startActivity(intent)
        }

        holder.btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditarViagens::class.java)
            intent.putExtra("viagem_id", viagem.id)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(viagem)
        }
    }

    override fun getItemCount(): Int = viagens.size
}
