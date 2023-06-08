package es.pacocovesgarcia.padeldist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EmailVerificationScreen : AppCompatActivity() {

    //Declaración de variables

    private lateinit var btnAccept : Button

    private lateinit var etEmailVerificationText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        ShowVerificationMessage()

        //Inicialización de variables

        btnAccept = findViewById(R.id.btnAccept)

        //Eventos de los botones

        btnAccept.setOnClickListener{
            BackToMainScreen()
        }

    }

    private fun BackToMainScreen() {
        val intent = Intent(this, LogInScreen::class.java)
        startActivity(intent)
    }

    private fun ShowVerificationMessage() {
        val Email = intent.getStringExtra("Email").toString()
        val maxLength = 6
        val ShorterEmail = if (Email.length > maxLength){
            val index = Email.indexOf("@")
            val username = Email.substring(0, maxLength.coerceAtMost(index))
            val domain = Email.substring(index)
            "$username...$domain"
        }else{
            Email
        }
        etEmailVerificationText = findViewById(R.id.etEmailVerificationText)
        etEmailVerificationText.text = "Se ha enviado un correo electrónico de confirmación a la dirección de correo: $ShorterEmail"
    }
}