package es.pacocovesgarcia.padeldist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.split
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import entities.Reserva
import org.w3c.dom.Text

class ReservaTransactionScreen : AppCompatActivity() {

    private lateinit var tvPistaId: TextView
    private lateinit var tvFechaValue: TextView
    private lateinit var tvDuracionValue : TextView
    private lateinit var tvFechaInicioValue : TextView
    private lateinit var tvFechaFinalValue : TextView
    private lateinit var tvValorTotalValue: TextView
    private lateinit var tvJugadorReservaValue: TextView

    private lateinit var btnReservar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva_transaction_screen)

        //Inicialización de variables
        tvPistaId = findViewById(R.id.tvPistaId)
        tvFechaValue = findViewById(R.id.tvFechaValue)
        tvFechaInicioValue = findViewById(R.id.tvFechaInicioValue)
        tvFechaFinalValue = findViewById(R.id.tvFechaFinalValue)
        tvJugadorReservaValue = findViewById(R.id.tvJugadorReservaValue)
        btnReservar = findViewById(R.id.btnReservar)

        tvPistaId.text = intent.getStringExtra("pista_nombre")
        val FechaInicio = intent.getStringExtra("hora_inicial")
        val FechaInicio2:String
        if (FechaInicio != null) {
            FechaInicio2 = FechaInicio.split("-")[0]
        }
        tvFechaInicioValue.text = FechaInicio
        val FechaFinal = intent.getStringExtra("hora_final")
        val FechaFinal2: String
        if (FechaFinal != null) {
            FechaFinal2 = FechaFinal.split("-")[1]
        }
        tvFechaFinalValue.text = FechaFinal
        tvFechaValue.text = intent.getStringExtra("dia_reserva")
        //tvJugadorReservaValue.text = Singletone.JugadorSingletone.LoggedPlayer.nombre

        //Toast personalizado
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout_root))
        val tvToast = layout.findViewById<TextView>(R.id.tvToast)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        btnReservar.setOnClickListener{
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("reservas")

            val fechaReserva = tvFechaValue.text.toString()
            val horaInicial = FechaInicio
            val horaFinal = FechaFinal
            val idPista = tvPistaId.text.toString()

            reference.orderByChild("fecha_reserva").equalTo(fechaReserva).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var reservaExistente = false

                    for (childSnapshot in dataSnapshot.children) {
                        val reserva = childSnapshot.getValue(Reserva::class.java)

                        if (reserva != null && reserva.hora_inicial == horaInicial && reserva.hora_final == horaFinal && reserva.id_pista == idPista) {
                            // Ya existe una reserva con los mismos datos
                            reservaExistente = true
                            break
                        }
                    }

                    if (reservaExistente) {
                        // Ya existe una reserva con los mismos datos
                        tvToast.text = "Ya existe una reserva realizada con estas características"
                        toast.show()
                    } else {
                        // No existe una reserva con los mismos datos, procede a insertar la reserva
                        val reservaData = HashMap<String, Any>()
                        reservaData["duracion_reserva"] = "1 hora"
                        reservaData["fecha_reserva"] = fechaReserva
                        reservaData["hora_final"] = FechaFinal.toString()
                        reservaData["hora_inicial"] = FechaInicio.toString()
                        reservaData["id_jugador"] = Singletone.JugadorSingletone.LoggedPlayer.jugador_id
                        reservaData["id_pista"] = idPista
                        val nuevaReservaReference = reference.push()
                        val nuevaReservaKey = nuevaReservaReference.key

                        if (nuevaReservaKey != null) {
                            reservaData["id_reserva"] = nuevaReservaKey

                            nuevaReservaReference.setValue(reservaData).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    tvToast.text = "Tu pista ha sido reservada"
                                    toast.show()
                                    val intent = Intent(this@ReservaTransactionScreen, HistorialReservasScreen::class.java)
                                    startActivity(intent)
                                } else {
                                    tvToast.text = "Error al reservar pista"
                                    toast.show()
                                    finish()
                                }
                            }
                        } else {
                            tvToast.text = "Error al reservar pista"
                            toast.show()
                            finish()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Ocurrió un error al realizar la consulta
                    tvToast.text = "Error al realizar la consulta"
                    toast.show()
                }
            })
        }
    }
}