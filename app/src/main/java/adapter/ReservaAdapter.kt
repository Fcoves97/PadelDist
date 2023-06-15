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

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDiaReserva: TextView = itemView.findViewById(R.id.tvDiaReserva)
        val tvPista: TextView = itemView.findViewById(R.id.tvPista)
        val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
    }

    interface OnVerHorariosClickListener {
        fun onVerHorariosClick(reserva: Reserva)
    }

    private var onVerHorariosClickListener: ReservaAdapter.OnVerHorariosClickListener? = null

    fun setOnVerHorariosClickListener(listener: ReservaAdapter.OnVerHorariosClickListener) {
        onVerHorariosClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]

        holder.tvDiaReserva.text = reserva.fecha_reserva
        holder.tvPista.text = reserva.id_pista
    }

    override fun getItemCount(): Int {
        return reservas.size
    }
}
