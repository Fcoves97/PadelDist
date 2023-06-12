package es.pacocovesgarcia.padeldist

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import entities.Configuracion
import es.pacocovesgarcia.padeldist.passwordMethods.ShowPassword
import es.pacocovesgarcia.padeldist.registrationAndLogInMethods.CheckLogInCredentials
import kotlinx.coroutines.launch

// LogInScreen.kt

class LogInScreen : AppCompatActivity(), LogInCallback {

    // Declaración de variables

    private lateinit var btnShowPassword: ImageButton
    private lateinit var btnRegister: Button
    private lateinit var btnLogIn : Button
    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText

    private val toast: Toast by lazy {
        val layoutInflater = layoutInflater
        val layout: View = layoutInflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout_root))
        val tvToast = layout.findViewById<TextView>(R.id.tvToast)
        Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
        }
    }

    private lateinit var logInCallback: LogInCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de variables

        btnShowPassword = findViewById(R.id.btnShowPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogIn = findViewById(R.id.btnLogIn)
        etUser = findViewById(R.id.etUser)
        etPassword = findViewById(R.id.etPassword)

        // Eventos de los botones

        btnShowPassword.setOnClickListener {
            ShowPassword(etPassword, btnShowPassword)
        }

        btnRegister.setOnClickListener {
            OpenRegisterScreen()
        }

        btnLogIn.setOnClickListener {
            if (etUser.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()) {
                logInCallback = this
                lifecycleScope.launch {
                    if (CheckLogInCredentials(etUser, etPassword,)) {
                        onLoginSuccess()
                        logInTransaction()
                    } else {
                        showToast("Los campos introducidos son incorrectos")
                    }
                }
            } else {
                showToast("Introduce ambos campos")
            }
        }
    }

    private fun showToast(message: String) {
        toast.view?.findViewById<TextView>(R.id.tvToast)?.text = message
        toast.show()
    }

    private fun logInTransaction() {
        val intent = Intent(this, WelcomeScreen::class.java)
        startActivity(intent)
    }

    private fun OpenRegisterScreen() {
        val intent = Intent(this, RegisterScreen::class.java)
        startActivity(intent)
    }

    override fun onLogInResult(success: Boolean) {
        if (success) {
            onLoginSuccess()
            logInTransaction()
        } else {
            showToast("Los campos introducidos son incorrectos")
        }
    }

    fun onLoginSuccess() {
        // Obtener una referencia a la base de datos
        val database = FirebaseDatabase.getInstance()
        val jugadoresRef = database.getReference("jugadores")
        val nombreJugador = Singletone.JugadorSingletone.LoggedPlayer.nombre // Reemplaza con el nombre real del jugador

        val query = jugadoresRef.orderByChild("nombre").equalTo(nombreJugador)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val idJugador = snapshot.key

                    if (idJugador != null) {
                        // Luego continúa con el siguiente paso para obtener los valores de configuración y configurar tu aplicación
                        obtenerValoresConfiguracion(idJugador)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error en caso de que ocurra
            }
        })
    }

    fun obtenerValoresConfiguracion(idJugador: String) {
        val database = FirebaseDatabase.getInstance()
        val configuracionesRef = database.getReference("configuraciones")
        val jugadorConfigRef = configuracionesRef.child(idJugador)

        jugadorConfigRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val configuracion = dataSnapshot.getValue(Configuracion::class.java)

                if (configuracion != null) {
                    // Configura tu aplicación con los valores obtenidos de la configuración
                    val temaSeleccionado = configuracion.tema.toString()
                    val volumenGeneral = configuracion.volumen_general
                    val notificarPartidas = configuracion.notificar_partidas
                    if (volumenGeneral != null) {
                        actualizarVolumen(volumenGeneral)
                    }
                    actualizarTema(temaSeleccionado)
                    recreate()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error en caso de que ocurra
            }
        })
    }

    private fun actualizarTema(temaSeleccionado: String) {
        if (temaSeleccionado == "Tema Oscuro") {
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

interface LogInCallback {
    fun onLogInResult(success: Boolean)
}


