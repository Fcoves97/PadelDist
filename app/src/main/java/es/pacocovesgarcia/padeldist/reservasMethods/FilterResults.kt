package es.pacocovesgarcia.padeldist.reservasMethods

import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import entities.Pista
import java.text.SimpleDateFormat

fun FilterResults(spinnerFecha: Spinner, spinnerHora: Spinner, context: Context) : List<Pista>?
{
    val fechaSeleccionada = spinnerFecha.selectedItem.toString()
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
    val fecha = formatoFecha.parse(fechaSeleccionada)

    val spinnerHora = spinnerHora.selectedItem.toString()

    // Realizar la petición con la base de datos utilizando los datos seleccionados
    val resultados = fecha?.let { searchCourtDatabaseMethod(it, spinnerHora, context) }

    // Mostrar los resultados en la parte inferior de la pantalla
    // Puedes utilizar una lista, RecyclerView, o cualquier otro método de visualización de datos
    return resultados
}