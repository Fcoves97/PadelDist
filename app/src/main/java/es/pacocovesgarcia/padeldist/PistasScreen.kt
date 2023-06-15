package es.pacocovesgarcia.padeldist

import adapter.PistaAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.coroutines.launch
import viewModel.PistasViewModel

class PistasScreen : AppCompatActivity(), PistaAdapter.OnVerHorariosClickListener {

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
        setContentView(R.layout.activity_pistas_screen)

        sideMenu = findViewById(R.id.SideMenu)
        btncloseMenu = findViewById(R.id.btnCloseMenu)
        btnOpenMenu = findViewById(R.id.btnOpenMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImage = findViewById(R.id.ivUserImage)
        tvUserName = findViewById(R.id.tvUserName)

        //Establecer opciones del menú y toolbar

        //SetUpMenuAndToolbar(dlMenu,btnOpenMenu,sideMenu,ivUserImage,tvUserName,btncloseMenu,this)

        dlMenu.closeDrawer(GravityCompat.START)
        dlMenu.visibility = View.INVISIBLE

        recyclerView = findViewById(R.id.recyclerViewPistas)
        adapter = PistaAdapter()

        pistasViewModel = ViewModelProvider(this)[PistasViewModel::class.java]

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        adapter.setOnVerHorariosClickListener(this)

        lifecycleScope.launch {
            val pistas = pistasViewModel.getListaPistas() // Obtener la lista de pistas
            adapter.updateData(pistas) // Actualizar el adaptador con la lista de pistas
            // Aquí puedes realizar cualquier otra acción que necesites con los datos de la lista de pistas
        }

       /* btnVerHorarios.setOnClickListener{
            val pistaId = pistaName // Obtener el ID de la pista seleccionada

            val dialogFragment = PistaDialogFragment()
            val args = Bundle()
            args.putString("pistaId", pistaId)
            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "pista_dialog")
        }*/
    }

    override fun onVerHorariosClick(pista: Pista) {
        val intent = Intent(this, HorarioReservasScreen::class.java)
        intent.putExtra("pista_nombre",pista.nombre_pista)
        startActivity(intent)
    }
}
