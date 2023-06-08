package dao

import androidx.room.*
import entities.JugadorConfig

@Dao
interface JugadorConfigDao {
    @Insert
    suspend fun insert(jugadorConfig: JugadorConfig)

    @Update
    suspend fun update(jugadorConfig: JugadorConfig)

    @Delete
    suspend fun delete(jugadorConfig: JugadorConfig)

    @Query("SELECT * FROM Jugadores_Configuraciones")
    suspend fun getAllJugadorConfigs(): List<JugadorConfig>

    @Query("SELECT * FROM Jugadores_Configuraciones WHERE id_user_config = :id")
    suspend fun getJugadorConfigById(id: Int): JugadorConfig?

    // Otros métodos de consulta según tus necesidades
}