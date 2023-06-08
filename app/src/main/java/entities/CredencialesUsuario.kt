package entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "credenciales_usuarios", indices = [Index(value = ["correo"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Jugador::class,
        parentColumns = ["id_jugador"],
        childColumns = ["id_jugador"],
        onDelete = ForeignKey.CASCADE)
    ])
class CredencialesUsuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var id_jugador: Int,
    val correo: String,
    val contraseña: String
)