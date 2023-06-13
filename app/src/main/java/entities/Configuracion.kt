package entities

class Configuracion(
    val id_jugador: String,
    val notificar_partidas: Boolean = false,
    val volumen_general: Int? = null,
    val tema: Tema = Tema.TEMA_CLARO
) {
    constructor() : this( "",false, 0, Tema.TEMA_CLARO)
    enum class Tema { TEMA_CLARO, TEMA_OSCURO }
}