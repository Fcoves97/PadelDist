package dao

import androidx.room.*
import entities.Jugador

@Dao
interface JugadorDao {
    @Insert
    fun insert(jugador: Jugador) : Long

    @Update
    suspend fun update(jugador: Jugador)

    @Delete
    suspend fun delete(jugador: Jugador)

    @Query("SELECT * FROM jugadores")
    fun getAllPlayers(): List<Jugador>

    @Query("SELECT * FROM jugadores WHERE id_jugador = :jugador_id")
    fun getPlayerById(jugador_id: Int): Jugador

    @Query("SELECT id_jugador FROM jugadores WHERE nombre = :nombre")
    fun getPlayerIdByName(nombre: String): Int

    @Query("SELECT COUNT(*) FROM jugadores WHERE nombre = :nombre")
    fun existsPlayerByName(nombre: String): Boolean
}