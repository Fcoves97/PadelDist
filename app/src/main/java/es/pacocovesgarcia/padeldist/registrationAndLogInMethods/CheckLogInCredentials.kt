package es.pacocovesgarcia.padeldist.registrationAndLogInMethods

import Singletone.JugadorSingletone
import android.util.Log
import android.widget.EditText
import com.google.firebase.database.*
import entities.Jugador
import es.pacocovesgarcia.padeldist.passwordMethods.EncryptPassword
import kotlinx.coroutines.coroutineScope

import com.google.firebase.auth.FirebaseAuth
import entities.CredencialesUsuario
import kotlinx.coroutines.tasks.await

// ...

// Función para iniciar sesión utilizando Firebase Authentication
var correo:String = ""
fun signInWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    onSuccess.invoke()
                } else {
                    onFailure.invoke("Error: Usuario nulo")
                }
            } else {
                onFailure.invoke("Error: ${task.exception?.message}")
            }
        }
}

// Función para verificar las credenciales del jugador
suspend fun CheckLogInCredentials(etUser: EditText, etPassword: EditText): Boolean = coroutineScope {
    val userName = etUser.text.toString()
    val password = etPassword.text.toString()

    val database = FirebaseDatabase.getInstance()
    val jugadoresRef = database.getReference("jugadores")

    try {
        val snapshot = jugadoresRef.orderByChild("nombre").equalTo(userName).limitToFirst(1).get().await()
        if (snapshot.exists()) {
            val playerId = snapshot.children.first().key
            val jugador = playerId?.let { obtenerJugadorPorId(it) }
            if (jugador != null) {
                val credencialesUsuario = obtenerCredencialesUsuarioPorId(jugador.jugador_id)
                if (credencialesUsuario != null && credencialesUsuario.contraseña == EncryptPassword(password)) {
                    JugadorSingletone.LoggedPlayer = jugador
                    JugadorSingletone.LoggedPlayerMail = correo
                    return@coroutineScope true
                }
            }
        }
        return@coroutineScope false
    } catch (e: Exception) {
        e.printStackTrace()
        return@coroutineScope false
    }
}

suspend fun obtenerJugadorPorId(idJugador: String): Jugador? {
    val database = FirebaseDatabase.getInstance()
    val jugadoresRef = database.getReference("jugadores")

    return try {
        val snapshot = jugadoresRef.child(idJugador).get().await()
        val jugador = snapshot.getValue(Jugador::class.java)
        jugador
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun obtenerCredencialesUsuarioPorId(idJugador: String): CredencialesUsuario? {
    val database = FirebaseDatabase.getInstance()
    val credencialesUsuarioRef = database.getReference("credenciales_usuarios")

     return try {
         return try {
             val snapshot = credencialesUsuarioRef.orderByChild("id_jugador").equalTo(idJugador).limitToFirst(1).get().await()
             if (snapshot.exists()) {
                 val credencialesUsuarioMap = snapshot.children.first().getValue() as? Map<String, Any>
                 val contraseña = credencialesUsuarioMap?.get("contraseña") as? String ?: ""
                 correo = credencialesUsuarioMap?.get("correo") as? String ?: ""
                 Log.d(contraseña,contraseña)
                 CredencialesUsuario(idJugador, correo, contraseña)
             } else {
                 null
             }
         } catch (e: Exception) {
             e.printStackTrace()
             null
         }
     } catch (e: Exception) {
         e.printStackTrace()
         null
     }
 }



