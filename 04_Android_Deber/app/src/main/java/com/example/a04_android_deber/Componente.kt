package com.example.a04_android_deber


import android.os.Parcelable

class Componente(
    val id: Int,
    val name: String,
    val description: String,
    val garantiaCo: Boolean,
    val precio: Double
): Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readDouble()
    ) {
    }

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeByte(if (garantiaCo) 1 else 0)
        parcel.writeDouble(precio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Componente> {
        override fun createFromParcel(parcel: android.os.Parcel): Componente {
            return Componente(parcel)
        }

        override fun newArray(size: Int): Array<Componente?> {
            return arrayOfNulls(size)
        }
    }
}