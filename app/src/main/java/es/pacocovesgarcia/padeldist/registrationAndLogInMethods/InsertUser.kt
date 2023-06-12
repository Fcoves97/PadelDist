package es.pacocovesgarcia.padeldist.registrationAndLogInMethods

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import entities.Configuracion
import entities.CredencialesUsuario
import entities.Jugador
import es.pacocovesgarcia.padeldist.R
import es.pacocovesgarcia.padeldist.imageConversions.convertDrawableToBase64
import es.pacocovesgarcia.padeldist.imageConversions.convertDrawableToByteArray
import es.pacocovesgarcia.padeldist.passwordMethods.EncryptPassword

fun InsertUser(
    context: Context, courtPosition: Jugador.Lado_Pista, level: Jugador.Nivel, userName: String,
    password: String, email: String, database: FirebaseDatabase
): Boolean {

    val jugadoresRef = database.getReference("jugadores")
    val credencialesUsuarioRef = database.getReference("credenciales_usuarios")
    val configuracionesRef = database.getReference("configuraciones")

    // Inicializar Firebase Authentication
    val auth = FirebaseAuth.getInstance()

    // Insertar jugador y sus credenciales
    try {
        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val jugadorId = user?.uid ?: ""

                    val jugadorInsertado = Jugador(
                        jugadorId,
                        userName,
                        level.toString(),
                        courtPosition.toString(),
                        0,
                        convertDrawableToBase64(context, R.drawable.default_user_image),
                        true
                    )

                    val configuracion = Configuracion(jugadorId, true, 100, Configuracion.Tema.CLARO)
                    configuracionesRef.child(jugadorId).setValue(configuracion)

                    val credencialesUsuario = CredencialesUsuario(
                        jugadorId,
                        email,
                        EncryptPassword(password)
                    )

                    val nuevaCredencialUsuarioRef = jugadoresRef.push()
                    val nuevaCredencialUsuarioId = nuevaCredencialUsuarioRef.key ?: ""

                    val updates = HashMap<String, Any>()
                    updates["jugadores/$jugadorId"] = jugadorInsertado
                    updates["credenciales_usuarios/$nuevaCredencialUsuarioId"] = credencialesUsuario

                    database.reference.updateChildren(updates)
                        .addOnSuccessListener {
                            Log.d("InsertarJugador", "Jugador y CredencialesUsuario insertados correctamente")
                        }
                        .addOnFailureListener { error ->
                            Log.d("InsertarJugador", "Error al insertar Jugador y CredencialesUsuario")
                        }
                } else {
                    Log.d("InsertarJugador", "Error al crear el usuario en Firebase Authentication")
                }
            }

        return true
    } catch (exception: Exception) {
        Log.e("RegistroError", "No se ha podido registrar", exception)
        return false
    }
}
