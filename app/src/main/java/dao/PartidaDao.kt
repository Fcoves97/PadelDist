package dao

import androidx.room.*
import entities.Partida

@Dao
interface PartidaDao {
    @Insert
    suspend fun insert(partida: Partida)

    @Update
    suspend fun update(partida: Partida)

    @Delete
    suspend fun delete(partida: Partida)

    @Query("SELECT * FROM Partidas")
    suspend fun getAllPartidas(): List<Partida>

    @Query("SELECT * FROM Partidas WHERE id_partida = :id")
    suspend fun getPartidaById(id: Int): Partida?

    // Otros métodos de consulta según tus necesidades
}
