package entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "Pistas")
class Pista(
    @PrimaryKey(autoGenerate = true)
    var id_pista: Int,
    var nombre_pista: String,
    val precio_por_hora: BigDecimal
)