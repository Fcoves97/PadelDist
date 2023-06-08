package es.pacocovesgarcia.padeldist.reservasMethods

import java.text.SimpleDateFormat
import java.util.*

fun GetAvailableDates(): List<String> {
        val fechasDisponibles = mutableListOf<String>()
        val calendario = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        for (i in 0 until 14) {
            val fecha = dateFormat.format(calendario.time)
            fechasDisponibles.add(fecha)
            calendario.add(Calendar.DAY_OF_MONTH, 1)
        }
        return fechasDisponibles
    }