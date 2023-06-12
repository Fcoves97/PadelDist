package es.pacocovesgarcia.padeldist.reservasMethods

import android.content.Context
import android.widget.CheckBox
import android.widget.Spinner
import entities.Pista
import java.text.SimpleDateFormat

suspend fun FilterResults() : List<Pista>
{
   /* val fechaSeleccionada = spinnerFecha.selectedItem.toString()
    val spinnerHora = spinnerHora.selectedItem.toString()*/

    var resultados = mutableListOf<Pista>()

    resultados = searchCourtDatabaseMethod() as MutableList<Pista>

    return resultados
}