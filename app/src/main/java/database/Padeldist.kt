package database

import entities.HorarioPista
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import converters.Converters
import dao.*
import entities.*

@Database(entities = [Configuracion::class, HorarioPista::class ,Jugador::class,
    JugadorConfig::class, Partida::class, Pista::class, Reserva::class, CredencialesUsuario::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class Padeldist : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun configuracionDao(): ConfiguracionDao
    abstract fun horarioPistaDao(): HorarioPistaDao
    abstract fun jugadorConfigDao(): JugadorConfigDao
    abstract fun partidaDao(): PartidaDao
    abstract fun pistaDao(): PistaDao
    abstract fun reservaDao(): ReservaDao
    abstract fun CredencialesUsuarioDao() : CredencialesUsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: Padeldist? = null

        fun getDatabase(context: Context): Padeldist {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Padeldist::class.java,
                    "padeldist"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}