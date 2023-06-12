package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import entities.Pista
import android.widget.TextView
import es.pacocovesgarcia.padeldist.R

class PistaAdapter : RecyclerView.Adapter<PistaAdapter.PistaViewHolder>() {

    interface OnVerHorariosClickListener {
        fun onVerHorariosClick(pista: Pista)
    }

    private var pistas: List<Pista> = emptyList()
    private var onVerHorariosClickListener: OnVerHorariosClickListener? = null

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

    fun updateData(pistas: List<Pista>) {
        this.pistas = pistas
        notifyDataSetChanged()
    }

    fun setOnVerHorariosClickListener(listener: OnVerHorariosClickListener) {
        onVerHorariosClickListener = listener
    }

    inner class PistaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val btnVerHorarios: Button = itemView.findViewById(R.id.btnVerHorarios)

        fun bind(pista: Pista) {
            itemView.findViewById<TextView>(R.id.tvNombre).text = pista.nombre_pista

            btnVerHorarios.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pista = pistas[position]
                    onVerHorariosClickListener?.onVerHorariosClick(pista)
                }
            }
        }
    }
}
