package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "jugadores", indices = [Index(value = ["nombre"], unique = true)])
class Jugador(
    @PrimaryKey(autoGenerate = true)
    val id_jugador: Int,
    val nombre: String,
    val nivel: Nivel,
    val lado_pista: Lado_Pista?,
    val partidas_jugadas: Int?,
    val imagen_perfil: ByteArray?
){
    enum class Nivel {
        PRINCIPIANTE, INTERMEDIO, EXPERTO
    }
    enum class Lado_Pista {
        DERECHA, REVÉS
    }
}