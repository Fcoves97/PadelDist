package es.pacocovesgarcia.padeldist.registrationAndLogInMethods

import Singletone.JugadorSingletone
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import dao.CredencialesUsuarioDao
import dao.JugadorDao
import entities.Jugador
import es.pacocovesgarcia.padeldist.passwordMethods.EncryptPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun CheckLogInCredentials(
    context: Context, etUser: EditText, etPassword: EditText,
    jugadorDao: JugadorDao, credencialesUsuarioDao: CredencialesUsuarioDao)
: Boolean {
    val playerLogged : Jugador
    val userName = etUser.text.toString()
    val password = etPassword.text.toString()

    val encryptedPassword = EncryptPassword(password)

    if (userName.isNullOrEmpty() && password.isNullOrEmpty()) {


        val userID = withContext(Dispatchers.IO) {
            jugadorDao.getPlayerIdByName(userName)
        }

        val userPassword = withContext(Dispatchers.IO) {
            credencialesUsuarioDao.getUserPassword(userID)
        }

        return if (encryptedPassword == userPassword) {

            playerLogged = withContext(Dispatchers.IO) {
                jugadorDao.getPlayerById(userID)
            }
            JugadorSingletone.setLoggedPlayer(playerLogged)

            Toast.makeText(context, "Bienvenido de nuevo $userName", Toast.LENGTH_LONG).show()
            true
        } else {
            Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
            false
        }
    } else {
        Toast.makeText(context, "Introduce ambos campos", Toast.LENGTH_LONG).show()
        return false
    }
}