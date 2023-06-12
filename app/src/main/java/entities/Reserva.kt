package entities

class Reserva(
    val id_reserva: Int,
    val id_pista: Int,
    val id_jugador: Int,
    val fecha_reserva: Long,
    val duracion_reserva: Int
)