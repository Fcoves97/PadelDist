package es.pacocovesgarcia.padeldist

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallesPistaActivity : AppCompatActivity() {

    private lateinit var tvNombre: TextView
    private lateinit var tvUbicacion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_pista)

        val nombre = intent.getStringExtra("nombre")
        val ubicacion = intent.getStringExtra("ubicacion")

        // Mostrar los detalles de la pista en la actividad
        tvNombre.text = nombre
        tvUbicacion.text = ubicacion

        // Lógica para la reserva de la pista
        // ...
    }
}
