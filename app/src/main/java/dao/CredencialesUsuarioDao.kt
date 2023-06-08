package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import entities.CredencialesUsuario

@Dao
interface CredencialesUsuarioDao {
    @Insert
    fun insert(CredencialesUsuario: CredencialesUsuario)

    @Query("SELECT * FROM credenciales_usuarios WHERE correo = :correo")
    suspend fun getJugadorPorCorreo(correo: String): CredencialesUsuario?

    @Query("SELECT * FROM credenciales_usuarios WHERE id = :id")
    suspend fun getJugadorPorId(id: Int): CredencialesUsuario?

    @Query("SELECT EXISTS(SELECT 1 FROM credenciales_usuarios WHERE correo = :correo)")
    suspend fun existsEmail(correo: String): Boolean

    @Query("SELECT contraseña FROM credenciales_usuarios WHERE id_jugador = :id_jugador")
    fun getUserPassword(id_jugador: Int): String?

}
