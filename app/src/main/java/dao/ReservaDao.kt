package dao

import androidx.room.*
import entities.Reserva

@Dao
interface ReservaDao {
    @Insert
    suspend fun insert(reserva: Reserva)

    @Update
    suspend fun update(reserva: Reserva)

    @Delete
    suspend fun delete(reserva: Reserva)

    @Query("SELECT * FROM reservas")
    suspend fun getAllReservas(): List<Reserva>

    @Query("SELECT * FROM reservas WHERE id_reserva = :id")
    suspend fun getReservaById(id: Int): Reserva?

    // Otros métodos de consulta según tus necesidades
}
