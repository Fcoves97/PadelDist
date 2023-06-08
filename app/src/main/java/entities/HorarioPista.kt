package entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import converters.Converters
import entities.Pista
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

@Entity(tableName = "Horario_pistas",
    foreignKeys = [
        ForeignKey(entity = Pista::class, parentColumns = ["id_pista"], childColumns = ["pista"])
    ]
)
class HorarioPista(
    @PrimaryKey(autoGenerate = true)
    val id_horarioPista: Int,
    val pista: Int,
    @TypeConverters(Converters::class) val dia_pista: Date,
    val hora_pista: String,
)
