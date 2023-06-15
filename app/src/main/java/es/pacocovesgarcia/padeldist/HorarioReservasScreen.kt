package es.pacocovesgarcia.padeldist

import adapter.HorarioAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import entities.Reserva
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class HorarioReservasScreen : AppCompatActivity() {
    private val horariosManana = listOf(
        "08:00-08:30", "08:30-09:00", "09:00-09:30","09:30-10:00", "10:00-10:30", "10:30-11:00", "11:00-11:30", "11:30-12:00", "12:00-12:30", "12:30-13:00"
    )
    private val horariosTarde = listOf(
        "15:00-15:30", "15:30-16:00", "16:00-16:30", "16:30-17:00", "17:00-17:30", "17:30-18:00", "18:00-18:30", "18:30-19:00", "19:00-19:30", "19:30-20:00", "20:00-20:30", "20:30-21:00"
    )
    private val horariosReservados: MutableSet<Reserva> = mutableSetOf()

    private lateinit var horarioAdapter: HorarioAdapter
    private lateinit var btnVolver: Button
    private lateinit var btnReservar: Button
    private lateinit var tvPistaId : TextView

    private lateinit var rvHorarioManana: RecyclerView
    private lateinit var rvHorarioTarde: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horario_reservas_screen)

        rvHorarioManana = findViewById(R.id.rvHorarioManana)
        rvHorarioTarde = findViewById(R.id.rvHorarioTarde)
        tvPistaId = findViewById(R.id.tvNumeroPista)

        btnVolver = findViewById(R.id.btnVolver)
        btnReservar = findViewById(R.id.btnReservar)

        val pista = intent.getStringExtra("pista_nombre")

        tvPistaId.text = pista

        //Toast personalizado
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout_root))
        val tvToast = layout.findViewById<TextView>(R.id.tvToast)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        btnVolver.setOnClickListener {
            finish()
        }

        btnReservar.setOnClickListener {
            val horasSeleccionadas = getHorasSeleccionadas(rvHorarioManana,rvHorarioTarde)
            if(horasSeleccionadas == null || horasSeleccionadas.size < 2){
                tvToast.text = "Debes seleccionar 1 hora mínimo para continuar"
                toast.show()
            }else{
                val horasSeleccionadasArray: Array<String> = horasSeleccionadas.toTypedArray()
                val horario1 = horasSeleccionadasArray[0]
                val horario2 = horasSeleccionadasArray[1]

                if (sonHorariosConsecutivos(horario1, horario2) || sonHorariosConsecutivos(horario2, horario1)) {

                    val horaInicio = horario1.split("-")[0]
                    val horaFinal = horario2.split("-")[1]

                    val fechaActual = LocalDate.now()

                    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val fechaFormateada = fechaActual.format(formatoFecha)

                    val intent = Intent(this, ReservaTransactionScreen::class.java)
                    intent.putExtra("pista_nombre",pista)
                    intent.putExtra("hora_final",horario2)
                    intent.putExtra("hora_inicial",horario1)
                    intent.putExtra("dia_reserva",fechaFormateada)
                    startActivity(intent)

                } else {
                    tvToast.text = "Las horas deben ser consecutivas"
                    toast.show()
                }
            }
        }

        val fecha = obtenerFechaActual()

        // Obtener los horarios reservados de la base de datos
        if (pista != null) {
            getHorariosReservados(pista,rvHorarioManana,rvHorarioTarde) { horariosReservados ->
                // Luego de obtener los horarios reservados, puedes llamar a la función para configurar el RecyclerView
                horarioAdapter.notifyDataSetChanged()
            }
        }
    }

    fun sonHorariosConsecutivos(horario1: String, horario2: String): Boolean {
        val horaFinal1 = horario1.split("-")[1] // Obtener la parte final del primer horario
        val horaInicio2 = horario2.split("-")[0] // Obtener la parte inicial del segundo horario

        return horaFinal1 == horaInicio2 // Comprobar si coinciden las horas
    }

    fun obtenerFechaActual(): String {
        val fechaActual = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fechaActual.format(formatter)
    }

    private fun getHorariosReservados(pistaId: String,rvHorarioManana:RecyclerView,rvHorarioTarde:RecyclerView, callback: (Set<String>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("reservas")
        val fechaActual = LocalDate.now()

        val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaFormateada = fechaActual.format(formatoFecha)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val data = childSnapshot.value as? HashMap<String, Any>
                    if(data != null){
                        if (data["fecha_reserva"] as String == fechaFormateada && data["id_pista"] as? String == pistaId) {

                            val horaFinal = data["hora_final"] as? String
                            val horaInicial = data["hora_inicial"] as? String

                            // Ejemplo: agregar los horarios reservados a una lista
                            if (horaInicial != null && horaFinal != null) {
                                val horarioReservado = Reserva("","","","","", horaInicial, horaFinal)
                                horariosReservados.add(horarioReservado)
                            }
                        }
                    }
                }
                setupRecyclerView(rvHorarioManana, horariosManana, horariosReservados)
                setupRecyclerView(rvHorarioTarde, horariosTarde, horariosReservados)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun setupRecyclerView(rvHorario: RecyclerView, horariosDisponibles: List<String>, horariosReservados: Set<Reserva>) {
        horarioAdapter = HorarioAdapter(horariosDisponibles, horariosReservados)
        val spanCount = 4
        val layoutManager = GridLayoutManager(this, spanCount)
        rvHorario.layoutManager = layoutManager
        rvHorario.adapter = horarioAdapter
    }

    private fun getHorasSeleccionadas(rvHorarioManana: RecyclerView, rvHorarioTarde: RecyclerView): Set<String> {
        val adapterManana = rvHorarioManana.adapter as HorarioAdapter
        val adapterTarde = rvHorarioTarde.adapter as HorarioAdapter

        val horasSeleccionadas = mutableSetOf<String>()
        horasSeleccionadas.addAll(adapterManana.getSelectedHoras())
        horasSeleccionadas.addAll(adapterTarde.getSelectedHoras())

        return horasSeleccionadas
    }
}