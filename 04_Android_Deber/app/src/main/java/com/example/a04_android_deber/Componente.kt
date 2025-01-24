package com.example.a04_android_deber

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Componente(
    val id: String,
    var nombre: String,
    var precio: Double,
    var stock: Int,
    var enGarantia: Boolean,
    var fechaFabricacion: LocalDate
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        LocalDate.parse(parcel.readString(), DateTimeFormatter.ISO_DATE)
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeInt(stock)
        parcel.writeByte(if (enGarantia) 1 else 0)
        parcel.writeString(fechaFabricacion.format(DateTimeFormatter.ISO_DATE))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Componente> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFromParcel(parcel: Parcel): Componente {
            return Componente(parcel)
        }

        override fun newArray(size: Int): Array<Componente?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Componente(id='$id', nombre='$nombre', precio=$precio, stock=$stock, enGarantia=$enGarantia, fechaFabricacion=$fechaFabricacion)"
    }
}
