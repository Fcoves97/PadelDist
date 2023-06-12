package es.pacocovesgarcia.padeldist.pistaDialogFragment

import adapter.HorarioAdapter
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import entities.Pista
import es.pacocovesgarcia.padeldist.R
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

        // Obtener los horarios disponibles
        getHorariosDisponibles { horariosDisponibles ->
            // Aquí puedes manejar los horarios disponibles como desees
            // Por ejemplo, puedes mostrarlos en un RecyclerView o realizar otras operaciones.
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = HorarioAdapter(horariosDisponibles)
        }

        val dialog = dialogBuilder.create()

        // Acciones de los botones

        btnReservar.setOnClickListener {
            // Verificar si se ha seleccionado un horario
            val horarioSeleccionado = (recyclerView.adapter as? HorarioAdapter)?.getSelectedHorario()
            if (horarioSeleccionado != null) {
                listener?.onPistaReservada(pistaId, horarioSeleccionado.hora)
            } else {
                Toast.makeText(requireContext(), "Selecciona un horario", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }


    override fun onDetach() {
        super.onDetach()
        // Limpia la referencia al listener
        listener = null
    }

    private fun getHorariosDisponibles(callback: (List<HorarioPista>) -> Unit) {
        val horariosDisponibles = mutableListOf<HorarioPista>()

        val horaInicialManana = "08:00"
        val horaFinalManana = "13:30"
        val horaInicialTarde = "16:00"
        val horaFinalTarde = "21:00"

        val pistaSeleccionada = pistaId

        if (pistaSeleccionada != null) {
            buscarPistaReservada(pistaSeleccionada) { horariosReservados ->
                // Filtra los horarios disponibles según los horarios reservados obtenidos de la base de datos
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
                    val horarioPistaManana = HorarioPista(horarioManana, false, "mañana", numfilaManana)
                    val reservadoManana = horariosReservados.any { it.hora == horarioManana }
                    if (!reservadoManana) {
                        horariosDisponiblesFiltrados.add(horarioPistaManana)
                        numfilaManana++
                    }
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
                    val horarioPistaTarde = HorarioPista(horarioTarde, false, "tarde", numfilaTarde)
                    val reservadoTarde = horariosReservados.any { it.hora == horarioTarde }
                    if (!reservadoTarde) {
                        horariosDisponiblesFiltrados.add(horarioPistaTarde)
                        numfilaTarde++
                    }
                    calendarTarde.add(Calendar.MINUTE, 30)
                }

                // Agrega la franja "Mañana" si hay horarios disponibles
                if (horariosDisponiblesFiltrados.any { it.franja_horaria == "mañana" }) {
                    val headerManana = HorarioPista("HORARIO DE MAÑANA", false, "mañana", 0)
                    horariosDisponibles.add(headerManana)
                }

                // Agrega los horarios disponibles de la mañana filtrados
                horariosDisponibles.addAll(horariosDisponiblesFiltrados.filter { it.franja_horaria == "mañana" })

                // Agrega la franja "Tarde" si hay horarios disponibles
                if (horariosDisponiblesFiltrados.any { it.franja_horaria == "tarde" }) {
                    val headerTarde = HorarioPista("HORARIO DE TARDE", false, "tarde", 0)
                    horariosDisponibles.add(headerTarde)
                }

                // Agrega los horarios disponibles de la tarde filtrados
                horariosDisponibles.addAll(horariosDisponiblesFiltrados.filter { it.franja_horaria == "tarde" })

                // Llama al callback con la lista de horarios disponibles
                callback(horariosDisponibles)
            }
        } else {
            // Llama al callback con una lista vacía si no hay una pista seleccionada
            callback(emptyList())
        }
    }

    private fun buscarPistaReservada(pistaSeleccionada: String, callback: (List<HorarioPista>) -> Unit) {
        val horariosReservados = mutableListOf<HorarioPista>()

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Horarios_pistas")

        val query = reference.orderByChild("id_pista").equalTo(pistaSeleccionada)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val horaInicial = childSnapshot.child("hora_inicial").value as String
                    val horaFinal = childSnapshot.child("hora_final").value as String

                    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val calendar = Calendar.getInstance()

                    val inicio = format.parse(horaInicial)
                    val fin = format.parse(horaFinal)

                    calendar.time = inicio
                    while (calendar.time.before(fin)) {
                        val horario = format.format(calendar.time)
                        val horarioPista = HorarioPista(horario, false, "hora", 0)
                        horariosReservados.add(horarioPista)
                        calendar.add(Calendar.MINUTE, 30)
                    }
                }

                callback(horariosReservados)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the query error if needed
                callback(emptyList())
            }
        })
    }

    private fun manejarDisponibilidadHorarios(horariosReservados: List<HorarioPista>) {
        // Filtra los horarios disponibles según los horarios reservados obtenidos de Firebase
        // y continúa con la lógica de construcción de la lista de horarios disponibles

        // Ejemplo de filtrado:
        val horariosDisponiblesFiltrados = mutableListOf<HorarioPista>()
        val horariosDisponibles = mutableListOf<HorarioPista>()

        // Itera sobre los horarios disponibles
        for (horarioDisponible in horariosDisponibles) {
            // Verifica si el horario está reservado
            val reservado = horariosReservados.any { it.hora == horarioDisponible.hora }

            // Si no está reservado, agrega el horario a la lista filtrada de horarios disponibles
            if (!reservado) {
                horariosDisponiblesFiltrados.add(horarioDisponible)
            }
        }

        // Continúa con la lógica de construcción de la lista de horarios disponibles utilizando horariosDisponiblesFiltrados
        // ...
    }

    data class HorarioPista(val hora: String, var isSelected: Boolean, var franja_horaria: String, var num_fila:Int)
}



