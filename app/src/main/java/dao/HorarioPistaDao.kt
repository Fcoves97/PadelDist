package dao

import entities.HorarioPista
import androidx.room.*

@Dao
interface HorarioPistaDao {
    @Insert
    suspend fun insert(horarioPista: HorarioPista)

    @Update
    suspend fun update(horarioPista: HorarioPista)

    @Delete
    suspend fun delete(horarioPista: HorarioPista)

    @Query("SELECT * FROM Horario_Pistas")
    suspend fun getAllHorarioPistas(): List<HorarioPista>

    @Query("SELECT * FROM Horario_Pistas WHERE id_horarioPista = :id")
    suspend fun getHorarioPistaById(id: Int): HorarioPista?

    // Otros métodos de consulta según tus necesidades
}