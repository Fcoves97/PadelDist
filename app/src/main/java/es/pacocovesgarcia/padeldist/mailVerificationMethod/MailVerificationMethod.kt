package es.pacocovesgarcia.padeldist.mailVerificationMethod

import java.util.Properties
import javax.mail.Session
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Message
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun sendVerificationEmail(email: String, verificationCode: String) : Boolean{
    val props = Properties().apply {
        this["mail.smtp.host"] = "smtp.gmail.com"
        this["mail.smtp.port"] = "587"
        this["mail.smtp.auth"] = "true"
        this["mail.smtp.starttls.enable"] = "true"
    }

    try {

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("PadelDist@gmail.com", "kpwy7tDx2M4qUBXs") // Reemplaza con tus credenciales de correo electrónico
            }
        })

        val message = MimeMessage(session)
        message.setFrom(InternetAddress("PadelDist@gmail.com")) // Reemplaza con tu dirección de correo electrónico
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
        message.subject = "Confirmación de correo electrónico"
        message.setText("Haz clic en el enlace para confirmar tu correo electrónico: https://www.example.com/confirm?code=$verificationCode")

        Transport.send(message)
        return true
    }
    catch (e: Exception) {
        e.printStackTrace()
        return false // Hubo un error al enviar el correo
    }
}
