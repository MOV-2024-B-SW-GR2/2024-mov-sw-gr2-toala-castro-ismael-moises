package com.example.a04_android_deber
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Computador (
    val id: Int,
    val name: String,
    val ram: Int,
    val garantia: Boolean,
    val debutCompra: LocalDate,
    val peso: Double,
    val latitude: Double,
    val longitude: Double
): Parcelable {
    val latitud: Double
        get() = latitude
    val longitud: Double
        get() = longitude

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        LocalDate.parse(parcel.readString(), DateTimeFormatter.ISO_DATE),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(ram)
        parcel.writeByte(if (garantia) 1 else 0)
        parcel.writeString(debutCompra.format(DateTimeFormatter.ISO_DATE))
        parcel.writeDouble(peso)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Computador> {

        override fun createFromParcel(parcel: Parcel): Computador {
            return Computador(parcel)
        }

        override fun newArray(size: Int): Array<Computador?> {
            return arrayOfNulls(size)
        }
    }
}