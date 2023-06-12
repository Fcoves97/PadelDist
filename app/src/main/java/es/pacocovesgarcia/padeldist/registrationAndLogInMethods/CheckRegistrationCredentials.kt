package es.pacocovesgarcia.padeldist.registrationAndLogInMethods

import android.util.Log
import android.util.Patterns
import android.widget.RadioGroup
import android.widget.TextView
import com.google.firebase.database.*
import entities.Jugador
import kotlinx.coroutines.*

suspend fun CheckRegistrationCredentials(
    UserName: String, Password: String, Password2: String, Email: String,
    tvMistakes: TextView, rgLevels: RadioGroup, rgPosition: RadioGroup, database: FirebaseDatabase
)
: Boolean {


        val specialCarac = "!@#\$%&*-_.;!?:\\\\/+=~"
        val numberRestric = "0123456789"

        val jugadoresRef = database.getReference("jugadores")
        val credencialesUsuarioRef = database.getReference("credenciales_usuarios")

        val existsPlayerName = checkIfUserNameExists(UserName, jugadoresRef)
        val existsPlayerEmail = checkIfEmailExists(Email, credencialesUsuarioRef)


        if (UserName.isEmpty()) {
            tvMistakes.text =("Introduce un nombre de usuario")
            return false
        }
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
        if (existsPlayerName) {
            tvMistakes.text = "Ya hay un usuario registrado con este nombre"
            return false
        }
        if (existsPlayerEmail) {
            tvMistakes.text = "Ya hay existe una cuenta vinculada a este correo"
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

suspend fun checkIfUserNameExists(userName: String, jugadoresRef: DatabaseReference): Boolean =
    suspendCancellableCoroutine { continuation ->
        val query = jugadoresRef.orderByChild("nombre").equalTo(userName)
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                continuation.resume(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                continuation.resume(false)
            }
        }

        query.addListenerForSingleValueEvent(listener)

        continuation.invokeOnCancellation {
            query.removeEventListener(listener)
        }
    }

private fun <T> CancellableContinuation<T>.resume(value: T) {
    if (isActive) {
        resumeWith(Result.success(value))
    }
}

suspend fun checkIfEmailExists(Email: String, credencialesUsuarioRef: DatabaseReference): Boolean =
    suspendCancellableCoroutine { continuation ->
        val query = credencialesUsuarioRef.orderByChild("correo").equalTo(Email)
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                continuation.resume(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                continuation.resume(false)
            }
        }

        query.addListenerForSingleValueEvent(listener)

        continuation.invokeOnCancellation {
            query.removeEventListener(listener)
        }
    }

