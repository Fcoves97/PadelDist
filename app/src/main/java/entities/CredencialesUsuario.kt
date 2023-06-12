package entities

class CredencialesUsuario(
    var id_jugador: String,
    val correo: String,
    val contraseña: String
){
    constructor() : this("","","")
}