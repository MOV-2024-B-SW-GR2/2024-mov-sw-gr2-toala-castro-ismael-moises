package com.example.a04_android_deber
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class Computador(
    val id: String,
    var nombre: String,
    var ram: Int,
    var garantia: Boolean,
    var fechaCompra: LocalDate,
    var peso: Double,
    private val componentes: MutableList<Componente> = mutableListOf()
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        LocalDate.parse(parcel.readString(), DateTimeFormatter.ISO_DATE),
        parcel.readDouble(),
        mutableListOf<Componente>().apply {
            parcel.readList(this, Componente::class.java.classLoader)
        }
    )

    override fun toString(): String {
        return nombre
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeInt(ram)
        parcel.writeByte(if (garantia) 1 else 0)
        parcel.writeString(fechaCompra.format(DateTimeFormatter.ISO_DATE))
        parcel.writeDouble(peso)
        parcel.writeList(componentes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Computador> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFromParcel(parcel: Parcel): Computador {
            return Computador(parcel)
        }

        override fun newArray(size: Int): Array<Computador?> {
            return arrayOfNulls(size)
        }
    }
}