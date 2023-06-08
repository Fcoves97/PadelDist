package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import entities.Pista
import android.widget.TextView
import es.pacocovesgarcia.padeldist.R

class PistaAdapter : RecyclerView.Adapter<PistaAdapter.PistaViewHolder>() {

    private var pistas: List<Pista> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PistaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pista, parent, false)
        return PistaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PistaViewHolder, position: Int) {
        val pista = pistas[position]
        holder.bind(pista)
    }

    override fun getItemCount(): Int {
        return pistas.size
    }

    fun submitList(pistas: List<Pista>) {
        this.pistas = pistas
        notifyDataSetChanged()
    }

    inner class PistaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pista: Pista) {
            // Bind data to views in the ViewHolder
            itemView.findViewById<TextView>(R.id.tvNombre).text = pista.nombre_pista
            // ...
        }
    }
}
