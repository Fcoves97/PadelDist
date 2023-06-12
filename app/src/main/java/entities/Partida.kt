package entities

class Partida(
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