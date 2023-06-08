package entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Jugadores_Configuraciones",
    foreignKeys = [
        ForeignKey(entity = Jugador::class, parentColumns = ["id_jugador"], childColumns = ["id_jugador"]),
        ForeignKey(entity = Configuracion::class, parentColumns = ["id_config"], childColumns = ["id_config"])
    ]
)
class JugadorConfig(
    @PrimaryKey(autoGenerate = true)
    val id_user_config: Int = 0,
    val id_jugador: Int,
    val id_config: Int
)