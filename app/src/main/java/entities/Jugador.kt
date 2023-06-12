package entities

class Jugador(
    val jugador_id : String = "",
    val nombre: String = "",
    val nivel: String = Nivel.PRINCIPIANTE.toString(),
    val lado_pista: String = "",
    val partidas_jugadas: Int = 0,
    var imagen_perfil: String? = "",
    val primera_vez_registrado: Boolean = true
){
    constructor() : this( "","", Nivel.PRINCIPIANTE.toString(), "", 0, "", false)
    enum class Nivel {
        PRINCIPIANTE, INTERMEDIO, EXPERTO
    }
    enum class Lado_Pista {
        DERECHA, REVÉS
    }
}