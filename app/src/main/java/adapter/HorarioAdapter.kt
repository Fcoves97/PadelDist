package adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import entities.HorarioPista
import es.pacocovesgarcia.padeldist.R
import es.pacocovesgarcia.padeldist.R.*
import es.pacocovesgarcia.padeldist.R.color.*
import es.pacocovesgarcia.padeldist.pistaDialogFragment.PistaDialogFragment

class HorarioAdapter(private val horarios: List<HorarioPista>) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    private val selectedPositions: MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.bind(horario, selectedPositions.contains(position))
    }

    override fun getItemCount(): Int {
        return horarios.size
    }

    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleSelection(position)
                }
            }
        }

        fun bind(horario: HorarioPista, isSelected: Boolean) {
            tvHorario.text = horario.dia_pista

            if (isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.lighterBrown))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.darkBrown))
            }
        }
    }

    private fun toggleSelection(position: Int) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
        } else {
            if (selectedPositions.size >= 3) {
                val removedPosition = selectedPositions.removeAt(0)
                notifyItemChanged(removedPosition)
            }
            selectedPositions.add(position)
        }
        notifyDataSetChanged()
    }

    private fun arePositionsConsecutive(): Boolean {
        if (selectedPositions.size != 3) {
            return false
        }

        val positions = selectedPositions.sorted()

        val firstFila = horarios[positions[0]].hora_final.toInt()
        val secondFila = horarios[positions[1]].hora_final.toInt()
        val thirdFila = horarios[positions[2]].hora_final.toInt()

        return (secondFila - firstFila == 1) && (thirdFila - secondFila == 1)
    }

    fun getSelectedHorario(): HorarioPista? {
        if (selectedPositions.size == 1) {
            val position = selectedPositions.first()
            return horarios.getOrNull(position)
        }
        return null
    }
}
