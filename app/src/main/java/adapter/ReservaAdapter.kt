package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import entities.Pista
import entities.Reserva
import es.pacocovesgarcia.padeldist.R

class ReservaAdapter(private val reservas: List<Reserva>) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    private var onVerHorariosClickListener: OnVerHorariosClickListener? = null

    interface OnVerHorariosClickListener {
        fun onVerHorariosClick(reserva: Reserva)
    }

    fun setOnVerHorariosClickListener(listener: OnVerHorariosClickListener) {
        onVerHorariosClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.bind(reserva)
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvPista: TextView = itemView.findViewById(R.id.tvPista)
        private val tvDiaReserva: TextView = itemView.findViewById(R.id.tvDiaReserva)
        private val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)

        init {
            btnVerDetalles.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val reserva = reservas[position]
                    onVerHorariosClickListener?.onVerHorariosClick(reserva)
                }
            }
        }

        fun bind(reserva: Reserva) {
            tvPista.text = reserva.id_pista
            tvDiaReserva.text = reserva.fecha_reserva
        }
    }
}

