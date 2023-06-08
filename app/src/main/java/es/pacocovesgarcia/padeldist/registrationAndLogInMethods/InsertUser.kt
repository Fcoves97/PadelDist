package es.pacocovesgarcia.padeldist.registrationAndLogInMethods

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import dao.CredencialesUsuarioDao
import dao.JugadorDao
import database.Padeldist
import entities.CredencialesUsuario
import entities.Jugador
import es.pacocovesgarcia.padeldist.R
import es.pacocovesgarcia.padeldist.imageConversions.convertDrawableToByteArray
import es.pacocovesgarcia.padeldist.passwordMethods.EncryptPassword
import kotlinx.coroutines.*

fun InsertUser(context: Context, courtPosition:Jugador.Lado_Pista, level:Jugador.Nivel, userName:String,
               password: String, email: String, jugadorDao: JugadorDao,
               credencialesUsuarioDao: CredencialesUsuarioDao)
: Boolean{

    //Insertar jugador y sus credenciales
    try {

        val insertedUser = Jugador(0, userName, level, courtPosition, 0,
            convertDrawableToByteArray(context , R.drawable.default_user_image)
        )

        val userCredential = CredencialesUsuario(0, 0, email, EncryptPassword(password))

        val database = Padeldist.getDatabase(context)

        runBlocking(Dispatchers.IO) {
            database.runInTransaction {
                val idJugador = jugadorDao.insert(insertedUser)
                userCredential.id_jugador = idJugador.toInt()
                credencialesUsuarioDao.insert(userCredential)
            }
        }
        return true
    } catch (exception: Exception) {
        Log.e(TAG, "No te has podido registrar", exception)
        return false
    }
}