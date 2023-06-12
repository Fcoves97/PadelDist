package es.pacocovesgarcia.padeldist.reservasMethods

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import entities.Pista
import kotlinx.coroutines.tasks.await
import java.util.*

suspend fun searchCourtDatabaseMethod(): List<Pista> {
    val database = FirebaseDatabase.getInstance()
    val pistasRef = database.getReference("pistas")

    val pistasList = mutableListOf<Pista>()

    try {
        val dataSnapshot = pistasRef.get().await()
        for (snapshot in dataSnapshot.children) {
            val pistaMap = snapshot.value as? Map<String, Any>
            val nombrePista = pistaMap?.get("nombre_pista") as? String ?: ""
            val precioPorHora = pistaMap?.get("precio_por_hora") as? Long ?: 0
            val pista = Pista().apply {
                this.nombre_pista = nombrePista
                this.precio_por_hora = precioPorHora
            }
            pistasList.add(pista)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return pistasList
}