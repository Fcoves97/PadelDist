package entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity(tableName = "Partidas",
    foreignKeys = [
        ForeignKey(entity = Pista::class, parentColumns = ["id_pista"], childColumns = ["id_pista"]),
        ForeignKey(entity = Jugador::class, parentColumns = ["id_jugador"], childColumns = ["id_jugador_1"]),
        ForeignKey(entity = Jugador::class, parentColumns = ["id_jugador"], childColumns = ["id_jugador_2"]),
        ForeignKey(entity = Jugador::class, parentColumns = ["id_jugador"], childColumns = ["id_jugador_3"]),
        ForeignKey(entity = Jugador::class, parentColumns = ["id_jugador"], childColumns = ["id_jugador_4"])
    ]
)
class Partida(
    @PrimaryKey(autoGenerate = true)
    val id_partida: Int,
    val id_pista: Int,
    val fecha_partida: Long,
    val duracion_partida: Int,
    val id_jugador_1: Int,
    val nombre_jugador_1: String,
    val id_jugador_2: Int,
    val nombre_jugador_2: String,
    val id_jugador_3: Int,
    val nombre_jugador_3: String,
    val id_jugador_4: Int,
    val nombre_jugador_4: String
)