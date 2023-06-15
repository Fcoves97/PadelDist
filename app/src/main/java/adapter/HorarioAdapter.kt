package adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import entities.Reserva
import es.pacocovesgarcia.padeldist.R
import org.threeten.bp.LocalTime

class HorarioAdapter(private val horarios: List<String>, private val horariosReservados: Set<Reserva>) :
    RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    private val selectedPositions: MutableSet<Int> = mutableSetOf()

    private val MAX_SELECTED_COUNT = 2

    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val toggleButton: ToggleButton = itemView.findViewById(R.id.toggleButton)

        init {
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (isChecked) {
                        // Verificar si el horario está reservado
                        val hora = horarios[position]
                        if (horariosReservados.any { reserva -> reserva.hora_final == hora.split("-")[0]
                                    || reserva.hora_inicial == hora.split("-")[0] }) {
                            toggleButton.isChecked = false
                            // Aquí puedes aplicar cambios visuales para indicar que el horario está reservado
                            // Por ejemplo, cambiar el color del ToggleButton o deshabilitarlo
                            toggleButton.setBackgroundColor(Color.RED)
                            toggleButton.isEnabled = false
                        } else if (selectedPositions.size < MAX_SELECTED_COUNT) {
                            selectedPositions.add(position)
                        } else {
                            toggleButton.isChecked = false
                            // Aquí puedes aplicar cambios visuales para indicar que se ha alcanzado el límite de selección
                            // Por ejemplo, mostrar un mensaje de error o deshabilitar los ToggleButtons adicionales
                        }
                    } else {
                        selectedPositions.remove(position)
                    }
                }
            }
        }

        fun bind(hora: String) {
            toggleButton.textOn = hora
            toggleButton.textOff = hora
            toggleButton.isChecked = selectedPositions.contains(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val hora = horarios[position]
        holder.bind(hora)

        if (horariosReservados.any { reserva ->
                hora == reserva.hora_final || hora == reserva.hora_inicial
            }) {
            holder.toggleButton.isEnabled = false
            holder.toggleButton.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int = horarios.size

    fun getSelectedHoras(): Set<String> {
        return selectedPositions.map { horarios[it] }.toSet()
    }
}

