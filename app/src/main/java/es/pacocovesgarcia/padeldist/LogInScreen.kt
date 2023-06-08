package es.pacocovesgarcia.padeldist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import dao.CredencialesUsuarioDao
import dao.JugadorDao
import dao.PistaDao
import database.Padeldist
import entities.Pista
import es.pacocovesgarcia.padeldist.passwordMethods.ShowPassword
import es.pacocovesgarcia.padeldist.registrationAndLogInMethods.CheckLogInCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogInScreen : AppCompatActivity() {

    //Declaración de variables

    private lateinit var btnShowPassword: ImageButton
    private lateinit var btnRegister: Button
    private lateinit var btnLogIn : Button
    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText

    private var rightCredentials:Boolean = false

    private lateinit var database: Padeldist
    private lateinit var jugadorDao: JugadorDao
    private lateinit var credencialesUsuarioDao: CredencialesUsuarioDao

    private val coroutine = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicialización de variables

        btnShowPassword = findViewById(R.id.btnShowPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogIn = findViewById(R.id.btnLogIn)
        etUser = findViewById(R.id.etUser)
        etPassword = findViewById(R.id.etPassword)

        database = Padeldist.getDatabase(this)
        jugadorDao = database.jugadorDao()
        credencialesUsuarioDao = database.CredencialesUsuarioDao()

        //Eventos de los botones

        btnShowPassword.setOnClickListener {
            ShowPassword(etPassword,btnShowPassword)
        }

        btnRegister.setOnClickListener{
            OpenRegisterScreen()
        }

        btnLogIn.setOnClickListener{
            coroutine.launch {
                rightCredentials = CheckLogInCredentials(applicationContext, etUser, etPassword, jugadorDao,
                    credencialesUsuarioDao)
                if(rightCredentials){
                    LogInTransaction()
                }
            }
        }
    }

    private fun LogInTransaction() {
        val intent = Intent(this, WelcomeScreen::class.java)
        startActivity(intent)
    }

    private fun OpenRegisterScreen() {
        val intent = Intent(this, RegisterScreen::class.java)
        startActivity(intent)
    }
}