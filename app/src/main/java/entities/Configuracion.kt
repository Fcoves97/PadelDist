package entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Configuraciones")
class Configuracion(
    @PrimaryKey(autoGenerate = true)
    val id_config: Int,
    val nombre: String,
    val notificar_partidas: Boolean = false,
    val volumen_general: Int? = null,
    val tema: Tema = Tema.CLARO
) {
    enum class Tema {
        CLARO, OSCURO
    }
}