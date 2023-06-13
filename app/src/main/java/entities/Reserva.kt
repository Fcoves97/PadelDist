package entities

class Reserva(
    val id_reserva: String,
    val id_pista: String,
    val id_jugador: String,
    val fecha_reserva: String,
    val duracion_reserva: String
){
    constructor() : this( "","", "", "", "")
}