package es.pacocovesgarcia.padeldist.reservasMethods

import android.widget.Spinner
import entities.Pista

suspend fun FilterResults(spinnerFecha:Spinner,spinnerHora:Spinner) : List<Pista>
{
    val fechaSeleccionada = spinnerFecha.selectedItem.toString()
    val horaSeleccionada = spinnerHora.selectedItem.toString()

    val resultados = searchCourtDatabaseMethod(fechaSeleccionada,horaSeleccionada) as MutableList<Pista>

    return resultados
}