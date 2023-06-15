package es.pacocovesgarcia.padeldist

import adapter.ReservaAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import entities.Reserva
import es.pacocovesgarcia.padeldist.menuAndToolbar.SetUpMenuAndToolbar

class HistorialReservasScreen : AppCompatActivity(), ReservaAdapter.OnVerHorariosClickListener {

    private lateinit var sideMenu: NavigationView
    private lateinit var btncloseMenu: ImageButton
    private lateinit var btnOpenMenu: ImageButton
    private lateinit var dlMenu: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var ivUserImage : ImageView
    private lateinit var tvUserName : TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter
    private lateinit var reservas: ArrayList<Reserva> // Lista de reservas
    private lateinit var tvPista : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_reservas_screen)

        sideMenu = findViewById(R.id.SideMenu)
        btncloseMenu = findViewById(R.id.btnCloseMenu)
        btnOpenMenu = findViewById(R.id.btnOpenMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImage = findViewById(R.id.ivUserImage)
        tvUserName = findViewById(R.id.tvUserName)

        //Establecer opciones del menú y toolbar
        SetUpMenuAndToolbar(
            dlMenu,
            btnOpenMenu,
            sideMenu,
            ivUserImage,
            tvUserName,
            btncloseMenu,
            this
        )

        dlMenu.closeDrawer(GravityCompat.START)
        dlMenu.visibility = View.INVISIBLE

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reservas = ArrayList()
        reservaAdapter = ReservaAdapter(reservas)
        recyclerView.adapter = reservaAdapter

        // Obtener referencia a las reservas en la base de datos
        val database = FirebaseDatabase.getInstance()
        val reservasRef = database.getReference("reservas")

        // Realizar la consulta
        val query = reservasRef.orderByChild("id_jugador")
            .equalTo(Singletone.JugadorSingletone.LoggedPlayer.jugador_id)

        // Escuchar los cambios en los datos
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (reservaSnapshot in dataSnapshot.children) {
                    val reserva = reservaSnapshot.getValue(Reserva::class.java)
                    reserva?.let {
                        reservas.add(it)
                    }
                }
                reservaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error en caso de que ocurra
            }
        })
    }

    override fun onVerHorariosClick(reserva: Reserva) {

    }
}