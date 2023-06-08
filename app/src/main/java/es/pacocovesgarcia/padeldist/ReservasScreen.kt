package es.pacocovesgarcia.padeldist

import adapter.PistaAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import entities.Pista
import es.pacocovesgarcia.padeldist.reservasMethods.FilterResults
import es.pacocovesgarcia.padeldist.reservasMethods.GetAvailableDates
import viewModel.PistasViewModel

class ReservasScreen : AppCompatActivity() {

    private lateinit var spinnerFecha: Spinner
    private lateinit var spinnerHora: Spinner
    private lateinit var btnVerResultados: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PistaAdapter
    private lateinit var pistasViewModel: PistasViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas_screen)


        spinnerFecha = findViewById(R.id.spinnerFecha)
        spinnerHora = findViewById(R.id.spinnerHora)
        btnVerResultados = findViewById(R.id.btnVerResultados)
        recyclerView = findViewById(R.id.recyclerViewPistas)
        adapter = PistaAdapter()

        pistasViewModel = ViewModelProvider(this)[PistasViewModel::class.java]

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        // Configurar opciones del Spinner de fechas
        val fechas = GetAvailableDates()
        val fechaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fechas)
        fechaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFecha.adapter = fechaAdapter

        // Configurar opciones del Spinner de horas
        val horas = resources.getStringArray(R.array.horas)
        val horasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horas)
        horasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHora.adapter = horasAdapter

        // Manejar evento de clic del botón
        btnVerResultados.setOnClickListener {
            val pistasDisponibles = FilterResults(spinnerFecha,spinnerHora,this)
            if (pistasDisponibles != null) {
                adapter.submitList(pistasDisponibles)
            }
        }
    }
}