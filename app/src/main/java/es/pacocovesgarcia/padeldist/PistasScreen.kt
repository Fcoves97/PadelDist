package es.pacocovesgarcia.padeldist

import adapter.PistaAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import viewModel.PistasViewModel

class PistasScreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PistaAdapter
    private lateinit var pistasViewModel: PistasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pistas_screen)

        recyclerView = findViewById(R.id.recyclerViewPistas)
        adapter = PistaAdapter()

        pistasViewModel = ViewModelProvider(this)[PistasViewModel::class.java]

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            val pistas = pistasViewModel.getListaPistas() // Obtener la lista de pistas
            adapter.submitList(pistas) // Actualizar el adaptador con la lista de pistas
            // Aquí puedes realizar cualquier otra acción que necesites con los datos de la lista de pistas
        }
    }
}
