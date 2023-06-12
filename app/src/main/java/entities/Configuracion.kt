package entities

class Configuracion(
    val id_jugador: String,
    val notificar_partidas: Boolean = false,
    val volumen_general: Int? = null,
    val tema: Tema = Tema.CLARO
) {
    constructor() : this( "",false, 0, Tema.CLARO)
    enum class Tema { CLARO, OSCURO }
}