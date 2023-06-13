package es.pacocovesgarcia.padeldist

import android.content.Context
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import entities.Configuracion
import es.pacocovesgarcia.padeldist.menuAndToolbar.SetUpMenuAndToolbar

class SettingsScreen : AppCompatActivity() {
    private lateinit var sideMenu: NavigationView
    private lateinit var btncloseMenu: ImageButton
    private lateinit var btnOpenMenu: ImageButton
    private lateinit var dlMenu: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var ivUserImage : ImageView
    private lateinit var tvUserName : TextView

    private lateinit var seekBar : SeekBar
    private lateinit var cbNotificar : CheckBox
    private lateinit var radioGroupTheme : RadioGroup

    private lateinit var btnAplicar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen)

        // Inicializar variables
        sideMenu = findViewById(R.id.SideMenu)
        btncloseMenu = findViewById(R.id.btnCloseMenu)
        btnOpenMenu = findViewById(R.id.btnOpenMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImage = findViewById(R.id.ivUserImage)
        tvUserName = findViewById(R.id.tvUserName)

        seekBar = findViewById(R.id.seekBar)
        cbNotificar = findViewById(R.id.cbNotificar)
        radioGroupTheme = findViewById(R.id.radioGroupTheme)

        btnAplicar = findViewById(R.id.btnAplicar)

        //Toast personalizado
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout_root))
        val tvToast = layout.findViewById<TextView>(R.id.tvToast)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        //Establecer opciones del menú y toolbar

        SetUpMenuAndToolbar(dlMenu,btnOpenMenu,sideMenu,ivUserImage,tvUserName,btncloseMenu,this)

        dlMenu.closeDrawer(GravityCompat.START)
        dlMenu.visibility = View.INVISIBLE

        inicializarAjustes()

        btnAplicar.setOnClickListener{
            val seekBarValue = seekBar.progress
            val notificarPartidas = cbNotificar.isChecked
            val temaSeleccionado = if (radioGroupTheme.checkedRadioButtonId == R.id.radioButtonDark)
                Configuracion.Tema.TEMA_OSCURO else Configuracion.Tema.TEMA_CLARO

            val database = FirebaseDatabase.getInstance()
            val configuracionesRef = database.getReference("configuraciones")
            val jugadoresRef = database.getReference("jugadores")

            val query = jugadoresRef.orderByChild("nombre").equalTo(Singletone.JugadorSingletone.LoggedPlayer.nombre)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val idJugador = snapshot.key
                        if (idJugador != null) {
                            val jugadorConfigRef = configuracionesRef.child(idJugador)

                            val actualizacionConfig = HashMap<String, Any>()
                            actualizacionConfig["notificar_partidas"] = notificarPartidas
                            actualizacionConfig["tema"] = temaSeleccionado
                            actualizacionConfig["volumen_general"] = seekBarValue

                            jugadorConfigRef.updateChildren(actualizacionConfig)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        actualizarVolumen(seekBarValue)
                                        actualizarTema(temaSeleccionado)
                                        recreate()
                                        tvToast.text = "Ajustes aplicados"
                                        toast.show()
                                    }
                                }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    tvToast.text = "Error al aplicar los ajustes"
                    toast.show()
                }
            })
        }
    }

    private fun inicializarAjustes() {
        val database = FirebaseDatabase.getInstance()
        val configuracionesRef = database.getReference("configuraciones")
        val jugadoresRef = database.getReference("jugadores")

        val query = jugadoresRef.orderByChild("nombre").equalTo(Singletone.JugadorSingletone.LoggedPlayer.nombre)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val idJugador = snapshot.key
                    if (idJugador != null) {
                        val jugadorConfigRef = configuracionesRef.child(idJugador)

                        jugadorConfigRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(configSnapshot: DataSnapshot) {
                                val configuraciones = configSnapshot.getValue(Configuracion::class.java)
                                configuraciones?.let {
                                    seekBar.progress = it.volumen_general!!
                                    cbNotificar.isChecked = it.notificar_partidas

                                    if (it.tema == Configuracion.Tema.TEMA_OSCURO) {
                                        radioGroupTheme.check(R.id.radioButtonDark)
                                    } else {
                                        radioGroupTheme.check(R.id.radioButtonLight)
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Manejar el error si es necesario
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error si es necesario
            }
        })
    }


    private fun actualizarTema(temaSeleccionado: Configuracion.Tema) {
        if (temaSeleccionado == Configuracion.Tema.TEMA_OSCURO) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme_Light)
        }
    }

    private fun actualizarVolumen(seekBarValue: Int) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val desiredVolume = (seekBarValue / 100f * maxVolume).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, desiredVolume, 0)
    }
}