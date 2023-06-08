package es.pacocovesgarcia.padeldist.registrationAndLogInMethods

import android.util.Patterns
import android.widget.RadioGroup
import android.widget.TextView
import dao.CredencialesUsuarioDao
import dao.JugadorDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun CheckRegistrationCredentials(UserName: String, Password: String, Password2: String, Email: String,
                             tvMistakes: TextView, rgLevels: RadioGroup, rgPosition: RadioGroup,
                             jugadorDao: JugadorDao, credencialesUsuarioDao: CredencialesUsuarioDao)
: Boolean {


        val specialCarac = "!@#\$%&*-_.;!?:\\\\/+=~"
        val numberRestric = "0123456789"

        /*if (UserName.isEmpty()) {
            tvMistakes.text =("Introduce un nombre de usuario")
            return false
        }*/
        if (UserName.any { it in specialCarac } || UserName.any { it in numberRestric } ||
            UserName.contains(" ")) {
            tvMistakes.text =("El nombre no puede contener caracteres especiales, números ni espacios")
            return false
        }
        if (UserName.length < 5) {
            tvMistakes.text =("El nombre no puede tener menos de 5 caracteres")
            return false
        }
        if (UserName.length > 15) {
            tvMistakes.text =("El nombre no puede contener más de 15 caracteres")
            return false
        }

        val existsPlayerName = withContext(Dispatchers.IO) {
            jugadorDao.existsPlayerByName(UserName)
        }

        if (existsPlayerName) {
            tvMistakes.text =("Ya hay un usuario registrado con este nombre")
            return false
        }

        val existsEmail = withContext(Dispatchers.IO) {
            credencialesUsuarioDao.existsEmail(Email)
        }

        if (existsEmail) {
            tvMistakes.text =("Ya existe un usuario registrado con este correo electrónico")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            tvMistakes.text =("Introduce un correo electrónico válido")
            return false
        }
        if (Password.isEmpty()) {
            tvMistakes.text =("Introduce una contraseña")
            return false
        }
        if (Password.length < 8) {
            tvMistakes.text =("La contraseña debe contener más de 8 caracteres")
            return false
        }
        if (Password.length > 20) {
            tvMistakes.text =("La contraseña no puede contener más de 20 caracteres")
            return false
        }
        if (!Password.any { it in specialCarac }) {
            tvMistakes.text =("La contraseña debe contener al menos algún carácter especial")
            return false
        }
        if (Password.contains(" ")) {
            tvMistakes.text =("La contraseña no puede contener espacios")
            return false
        }
        if (Password2.isEmpty()) {
            tvMistakes.text =("Repite la contraseña introducida")
            return false
        }
        if (Password2 != Password) {
            tvMistakes.text =("Las contraseñas no coinciden")
            return false
        }
        if (rgPosition.checkedRadioButtonId == -1) {
            tvMistakes.text =("Selecciona una posición en la pista")
            return false
        }
        if (rgLevels.checkedRadioButtonId == -1) {
            tvMistakes.text = ("Selecciona tu nivel en la pista")
            return false
        }
    return true
}