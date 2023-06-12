package es.pacocovesgarcia.padeldist

import adapter.PistaAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import entities.Pista
import es.pacocovesgarcia.padeldist.menuAndToolbar.SetUpMenuAndToolbar
import es.pacocovesgarcia.padeldist.pistaDialogFragment.PistaDialogFragment
import es.pacocovesgarcia.padeldist.reservasMethods.FilterResults
import es.pacocovesgarcia.padeldist.reservasMethods.GetAvailableDates
import kotlinx.coroutines.launch
import viewModel.PistasViewModel

class ReservasScreen : AppCompatActivity(), PistaAdapter.OnVerHorariosClickListener  {

    private lateinit var spinnerFecha: Spinner
    private lateinit var spinnerHora: Spinner
    private lateinit var cbNoHourFilter: CheckBox
    private lateinit var btnVerResultados: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PistaAdapter
    private lateinit var pistasViewModel: PistasViewModel

    private lateinit var sideMenu: NavigationView
    private lateinit var btncloseMenu: ImageButton
    private lateinit var btnOpenMenu: ImageButton
    private lateinit var dlMenu: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var ivUserImage : ImageView
    private lateinit var tvUserName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas_screen)

        // Inicializar variables
        spinnerFecha = findViewById(R.id.spinnerFecha)
        spinnerHora = findViewById(R.id.spinnerHora)
        cbNoHourFilter = findViewById(R.id.cbNoHourFilter)
        btnVerResultados = findViewById(R.id.btnVerResultados)
        recyclerView = findViewById(R.id.recyclerViewPistas)
        adapter = PistaAdapter()


        sideMenu = findViewById(R.id.SideMenu)
        btncloseMenu = findViewById(R.id.btnCloseMenu)
        btnOpenMenu = findViewById(R.id.btnOpenMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImage = findViewById(R.id.ivUserImage)
        tvUserName = findViewById(R.id.tvUserName)

        //Establecer opciones del menú y toolbar

        SetUpMenuAndToolbar(dlMenu,btnOpenMenu,sideMenu,ivUserImage,tvUserName,btncloseMenu,this)

        dlMenu.closeDrawer(GravityCompat.START)
        dlMenu.visibility = View.INVISIBLE

        pistasViewModel = ViewModelProvider(this)[PistasViewModel::class.java]

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        adapter.setOnVerHorariosClickListener(this)

        // Configurar opciones del Spinner de fechas
        val fechas = GetAvailableDates()
        val fechaAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_layout, fechas)
        fechaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFecha.adapter = fechaAdapter

        // Configurar opciones del Spinner de horas
        val horas = resources.getStringArray(R.array.horas)
        val horasAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_layout, horas)
        horasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHora.adapter = horasAdapter

        cbNoHourFilter.setOnCheckedChangeListener{_, isChecked ->
            spinnerHora.isEnabled = !isChecked
        }

        // Manejar evento de clic del botón
        btnVerResultados.setOnClickListener {
            lifecycleScope.launch {
                val pistasDisponibles = FilterResults()
                adapter.updateData(pistasDisponibles)
            }
        }
    }

    override fun onVerHorariosClick(pista: Pista) {
        val dialogFragment = PistaDialogFragment.newInstance(pista)
        dialogFragment.show(supportFragmentManager, "pista_dialog")
    }
}