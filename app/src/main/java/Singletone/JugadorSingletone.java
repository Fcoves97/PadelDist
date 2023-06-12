package Singletone;

import entities.Jugador;

public class JugadorSingletone {
    public static Jugador LoggedPlayer;
    public static String LoggedPlayerMail;
    public static Jugador getLoggedPlayer() {
        return LoggedPlayer;
    }
    public static void setLoggedPlayer(Jugador loggedPlayer) {
        JugadorSingletone.LoggedPlayer = loggedPlayer;
    }
}
