package entities

import android.os.Parcel
import android.os.Parcelable

class Reserva(
    val id_reserva: String?,
    val id_pista: String?,
    val id_jugador: String?,
    val fecha_reserva: String?,
    val duracion_reserva: String?,
    val hora_inicial: String?,
    val hora_final: String?,
): Parcelable {
    constructor() : this("", "", "", "", "", "", "")

    // Implementación de Parcelable
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(duracion_reserva)
        parcel.writeString(fecha_reserva)
        parcel.writeString(hora_final)
        parcel.writeString(hora_inicial)
        parcel.writeString(id_jugador)
        parcel.writeString(id_pista)
        parcel.writeString(id_reserva)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reserva> {
        override fun createFromParcel(parcel: Parcel): Reserva {
            return Reserva(parcel)
        }

        override fun newArray(size: Int): Array<Reserva?> {
            return arrayOfNulls(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
}