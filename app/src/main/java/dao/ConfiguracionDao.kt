package dao

import androidx.room.*
import entities.Configuracion

@Dao
interface ConfiguracionDao {
    @Insert
    suspend fun insert(configuracion: Configuracion)

    @Update
    suspend fun update(configuracion: Configuracion)

    @Delete
    suspend fun delete(configuracion: Configuracion)

    @Query("SELECT * FROM configuraciones")
    suspend fun getAllConfiguraciones(): List<Configuracion>

    @Query("SELECT * FROM configuraciones WHERE id_config = :id")
    suspend fun getConfiguracionById(id: Int): Configuracion?

    // Otros métodos de consulta según tus necesidades
}