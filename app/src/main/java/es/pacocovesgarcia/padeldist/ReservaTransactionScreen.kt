package es.pacocovesgarcia.padeldist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        tvFechaInicioValue.text = intent.getStringExtra("hora_inicial")
        tvFechaFinalValue.text = intent.getStringExtra("hora_final")
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

        }
    }
}