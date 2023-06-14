package entities
class HorarioPista(
    val dia_pista: String,
    val hora_final: String,
    val hora_inicial: String,
    val id_pista: String
){
    constructor() : this( "","", "", "")
}
