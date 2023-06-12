package es.pacocovesgarcia.padeldist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import entities.Jugador
import es.pacocovesgarcia.padeldist.mailVerificationMethod.sendVerificationEmail
import es.pacocovesgarcia.padeldist.passwordMethods.ShowPassword
import es.pacocovesgarcia.padeldist.registrationAndLogInMethods.CheckRegistrationCredentials
import es.pacocovesgarcia.padeldist.registrationAndLogInMethods.InsertUser
import kotlinx.coroutines.*
import java.util.*

class RegisterScreen : AppCompatActivity() {

    //Declaración de variables

    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPassword2: EditText
    private lateinit var etEmail: EditText
    private lateinit var rgPosition: RadioGroup
    private lateinit var rbRight : RadioButton
    private lateinit var rbLeft: RadioButton
    private lateinit var rgLevels: RadioGroup
    private lateinit var rbBeginner: RadioButton
    private lateinit var rbMedium: RadioButton
    private lateinit var rbExpert : RadioButton
    private lateinit var btnAccept: Button
    private lateinit var btnCancel: Button
    private lateinit var btnShowPassword: ImageButton
    private lateinit var tvMistakes: TextView

    //Variable de orden de funciones del evento del boton btnAccept

    private var rightCredentials: Boolean = false

    //Variables del usuario para comprobar

    private lateinit var userName: String
    private lateinit var password: String
    private lateinit var password2: String
    private lateinit var email: String
    private lateinit var courtPosition: Jugador.Lado_Pista
    private lateinit var level: Jugador.Nivel

    // Variables del Email

    private val senderEmail = "PadelDist@gmail.com"
    private val senderPassword = "kpwy7tDx2M4qUBXs"

    //Coorutinas
    private val coroutine = CoroutineScope(Dispatchers.Main)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        //Inicialización de variables

        val database = FirebaseDatabase.getInstance()

        etUser = findViewById(R.id.etUser)
        etPassword = findViewById(R.id.etPassword)
        etPassword2 = findViewById(R.id.etPassword2)
        etEmail = findViewById(R.id.etEmail)
        rgPosition = findViewById(R.id.rgPosition)
        rbLeft = findViewById(R.id.rbLeft)
        rbRight = findViewById(R.id.rbRight)
        rgLevels = findViewById(R.id.rgLevels)
        rbBeginner = findViewById(R.id.rbBeginner)
        rbMedium = findViewById(R.id.rbMedium)
        rbExpert = findViewById(R.id.rbExpert)
        btnAccept = findViewById(R.id.btnAccept)
        btnCancel = findViewById(R.id.btnCancel)
        btnShowPassword = findViewById(R.id.btnShowPassword)
        tvMistakes = findViewById(R.id.tvMistakes)

        //Toast Personalizado
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout_root))
        val tvToast = layout.findViewById<TextView>(R.id.tvToast)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        //Eventos de los botones

        btnAccept.setOnClickListener {
            coroutine.launch {
                LoadCredentialsVariables()
                rightCredentials = CheckRegistrationCredentials(userName, password, password2,
                    email, tvMistakes, rgLevels, rgPosition, database)
                if (rightCredentials) {
                    tvMistakes.text = ""
                    //SendVerificationEmail()
                    //OpenEmailVerificationScreen()
                    if(InsertUser(applicationContext, courtPosition, level, userName, password, email,
                        database)){
                        tvToast.text = "Usuario registrado correctamente!"
                        toast.show()
                    }else{
                        tvToast.text = "No te has podido registrar"
                        toast.show()
                    }
                }
            }
        }

        btnCancel.setOnClickListener{
            finish()
        }

        btnShowPassword.setOnClickListener {
            ShowPassword(etPassword,btnShowPassword)
        }

        /*val verificationCode = UUID.randomUUID().toString()
        if(sendVerificationEmail("fcoves97@gmail.com",verificationCode)){
            Toast.makeText(applicationContext, "Se ha enviado", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext, "No se ha enviado", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun LoadCredentialsVariables() {

        userName = etUser.text.toString()
        password = etPassword.text.toString()
        password2 = etPassword2.text.toString()
        email = etEmail.text.toString().trim()
        courtPosition = if (rgPosition.checkedRadioButtonId == rbLeft.id) {
            Jugador.Lado_Pista.REVÉS
        } else {
            Jugador.Lado_Pista.DERECHA
        }
        level = if (rgLevels.checkedRadioButtonId == rbBeginner.id) {
            Jugador.Nivel.PRINCIPIANTE
        } else if (rgLevels.checkedRadioButtonId == rbMedium.id) {
            Jugador.Nivel.INTERMEDIO
        } else {
            Jugador.Nivel.EXPERTO
        }
    }

    private fun SendVerificationEmail() {
        val verificationCode = UUID.randomUUID().toString()
        sendVerificationEmail(etEmail.text.toString(),verificationCode)
    }

    private fun OpenEmailVerificationScreen() {
        val intent = Intent(this, EmailVerificationScreen::class.java)
        intent.putExtra("Email", etEmail.text.toString().trim())
        startActivity(intent)
    }
}