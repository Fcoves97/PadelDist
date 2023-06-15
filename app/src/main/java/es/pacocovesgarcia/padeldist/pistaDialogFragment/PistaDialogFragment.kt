package es.pacocovesgarcia.padeldist.pistaDialogFragment

import adapter.HorarioAdapter
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.jakewharton.threetenabp.AndroidThreeTen
import entities.HorarioPista
import entities.Pista
import es.pacocovesgarcia.padeldist.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*


class PistaDialogFragment : DialogFragment() {

    // Define una propiedad para almacenar el ID de la pista seleccionada
    private var pistaId: String? = null

    companion object {
        fun newInstance(pista: Pista): PistaDialogFragment {
            val fragment = PistaDialogFragment()
            val args = Bundle()
            args.putString("pistaId", pista.nombre_pista)
            fragment.arguments = args
            return fragment
        }
    }

    // Crea una interfaz para la comunicación con la actividad principal
    interface PistaDialogListener {
        fun onPistaReservada(pistaId: String?, horario: String)
    }

    // Crea una referencia nullable a la interfaz PistaDialogListener
    private var listener: PistaDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Asigna la actividad como el listener
        listener = context as? PistaDialogListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtiene el ID de la pista del argumento
        pistaId = arguments?.getString("pistaId")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // Inflar el diseño personalizado
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_pista, null)
        dialogBuilder.setView(view)

        val pistaIdTextView = view.findViewById<TextView>(R.id.tvPistaId)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val btnReservar = view.findViewById<Button>(R.id.btnReservar)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)

        pistaIdTextView.text = "Número de pista: $pistaId"

        val context = requireContext()
        val application = context.applicationContext as Application
        AndroidThreeTen.init(application)

        GlobalScope.launch(Dispatchers.Main) {
            val horariosDisponibles = getHorariosDisponibles()

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
           // val horarioAdapter = HorarioAdapter(horariosDisponibles)
           // recyclerView.adapter = horarioAdapter
           // horarioAdapter.notifyDataSetChanged()

            // Asigna el adaptador y notifica los cambios después de obtener los horarios disponibles
        }

        // Mueve estas líneas fuera del bloque GlobalScope.launch
        //val horarioAdapter = HorarioAdapter(emptyList())
        //recyclerView.adapter = horarioAdapter


        val dialog = dialogBuilder.create()

        // Acciones de los botones

        btnReservar.setOnClickListener {
            /*// Verificar si se ha seleccionado un horario
           // val horarioSeleccionado = (recyclerView.adapter as? HorarioAdapter)?.getSelectedHorario()
           // if (horarioSeleccionado != null) {
           //     listener?.onPistaReservada(pistaId, horarioSeleccionado.dia_pista)
            } else {
                Toast.makeText(requireContext(), "Selecciona un horario", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()*/
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }
    suspend fun getHorasNoDisponiblesDeHoy(pistaId: String?): MutableList<HorarioPista> = suspendCoroutine { continuation ->
        val horariosNoDisponibles = mutableListOf<HorarioPista>()

        val fechaActual = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fechaActualFormateada = fechaActual.format(formatter)

        val database = FirebaseDatabase.getInstance()
        val referencia = database.getReference("horarios_pistas")
        val query = referencia.orderByChild("dia_pista").equalTo("11/06/2023")

        val task = query.get()
        task.addOnSuccessListener { dataSnapshot ->
            // Recorre los nodos del resultado de la consulta
            dataSnapshot.children.forEach { snapshot ->
                val reserva = snapshot.getValue(HorarioPista::class.java)
                if (reserva?.id_pista == pistaId) {
                    // Aquí puedes guardar los valores de hora_inicial y hora_final
                    val horaInicial = reserva?.hora_inicial
                    val horaFinal = reserva?.hora_final

                    val horarioPista = horaInicial?.let {
                        if (horaFinal != null) {
                            HorarioPista("", horaFinal, it, pistaId.toString())
                        } else {
                            null
                        }
                    }

                    if (horarioPista != null) {
                        // Agregar el objeto HorarioPista a la lista horariosNoDisponibles
                        horariosNoDisponibles.add(horarioPista)
                    }
                }
            }

            // Devolver los resultados a través de la continuación
            continuation.resume(horariosNoDisponibles)
        }.addOnFailureListener { exception ->
            // Manejar el error aquí
            continuation.resumeWithException(exception)
        }
    }


    private suspend fun getHorariosDisponibles(): List<HorarioPista> = suspendCoroutine { continuation ->
        val horaInicialManana = "08:00"
        val horaFinalManana = "13:30"
        val horaInicialTarde = "15:00"
        val horaFinalTarde = "21:00"

        val horariosDisponiblesFiltrados = mutableListOf<HorarioPista>()

        // Agrega los horarios disponibles de la mañana
        val calendarManana = Calendar.getInstance()
        calendarManana.time = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(horaInicialManana)!!

        var numfilaManana: Int = 0
        while (calendarManana.get(Calendar.HOUR_OF_DAY) < horaFinalManana.split(":")[0].toInt() ||
            (calendarManana.get(Calendar.HOUR_OF_DAY) == horaFinalManana.split(":")[0].toInt() &&
                    calendarManana.get(Calendar.MINUTE) <= horaFinalManana.split(":")[1].toInt())
        ) {
            val horarioManana = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendarManana.time)
            val horarioPistaManana = HorarioPista("", horarioManana, "", numfilaManana.toString())
            horariosDisponiblesFiltrados.add(horarioPistaManana)
            numfilaManana++
            calendarManana.add(Calendar.MINUTE, 30)
        }

        // Agrega los horarios disponibles de la tarde
        val calendarTarde = Calendar.getInstance()
        calendarTarde.time = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(horaInicialTarde)!!

        var numfilaTarde: Int = numfilaManana
        while (calendarTarde.get(Calendar.HOUR_OF_DAY) < horaFinalTarde.split(":")[0].toInt() ||
            (calendarTarde.get(Calendar.HOUR_OF_DAY) == horaFinalTarde.split(":")[0].toInt() &&
                    calendarTarde.get(Calendar.MINUTE) <= horaFinalTarde.split(":")[1].toInt())
        ) {
            val horarioTarde = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendarTarde.time)
            val horarioPistaTarde = HorarioPista("", horarioTarde, "", numfilaTarde.toString())
            horariosDisponiblesFiltrados.add(horarioPistaTarde)
            numfilaTarde++
            calendarTarde.add(Calendar.MINUTE, 30)
        }
        continuation.resume(horariosDisponiblesFiltrados)
    }
    override fun onDetach() {
        super.onDetach()
        // Limpia la referencia al listener
        listener = null
    }
}



