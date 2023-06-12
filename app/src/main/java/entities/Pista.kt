package entities

import android.os.Parcel
import android.os.Parcelable

class Pista() : Parcelable {
    var nombre_pista: String = ""
    var precio_por_hora: Long = 0

    constructor(parcel: Parcel) : this() {
        nombre_pista = parcel.readString() ?: ""
        precio_por_hora = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre_pista)
        parcel.writeLong(precio_por_hora)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pista> {
        override fun createFromParcel(parcel: Parcel): Pista {
            return Pista(parcel)
        }

        override fun newArray(size: Int): Array<Pista?> {
            return arrayOfNulls(size)
        }
    }
}
