package dao

import androidx.room.*
import converters.Converters
import entities.Pista
import java.util.*

@Dao
interface PistaDao {
    @Insert
    suspend fun insert(pista: Pista)

    @Update
    suspend fun update(pista: Pista)

    @Delete
    suspend fun delete(pista: Pista)

    @Query("SELECT * FROM pistas")
    suspend fun getAllPistas(): List<Pista>

    @Query("SELECT * FROM pistas WHERE id_pista = :id")
    suspend fun getPistaById(id: Int): Pista?

    @Query("SELECT * FROM Pistas WHERE id_pista NOT IN (SELECT pista FROM Horario_pistas WHERE dia_pista = :fecha AND hora_pista = :hora)")
    fun getAvailableCourts(fecha: String, hora: String): List<Pista>
}
