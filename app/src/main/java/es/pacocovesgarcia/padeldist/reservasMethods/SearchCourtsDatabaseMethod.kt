package es.pacocovesgarcia.padeldist.reservasMethods

import android.content.Context
import converters.Converters
import dao.PistaDao
import database.Padeldist
import entities.Pista
import java.util.*

fun searchCourtDatabaseMethod(fecha: Date, hora: String, context: Context): List<Pista> {
    val database = Padeldist.getDatabase(context)
    val pistasDao = database.pistaDao()

    return pistasDao.getAvailableCourts(fecha.toString(), hora)
}