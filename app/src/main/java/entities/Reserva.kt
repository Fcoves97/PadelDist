package entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Reservas", foreignKeys = [
    ForeignKey(entity = Pista::class, parentColumns = ["id_pista"], childColumns = ["id_pista"]),
    ForeignKey(entity = Jugador::class, parentColumns = ["id_jugador"], childColumns = ["id_jugador"])
])
class Reserva(
    @PrimaryKey(autoGenerate = true)
    val id_reserva: Int,
    val id_pista: Int,
    val id_jugador: Int,
    val fecha_reserva: Long,
    val duracion_reserva: Int
)