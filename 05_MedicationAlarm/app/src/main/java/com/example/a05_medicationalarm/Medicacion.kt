package com.example.a05_medicationalarm
import android.os.Parcelable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Medicacion(
    val id: Int,
    val name: String,
    val description: String,
    val dosis: Double
): Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
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
        parcel.writeDouble(dosis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Medicacion> {
        override fun createFromParcel(parcel: android.os.Parcel): Medicacion {
            return Medicacion(parcel)
        }

        override fun newArray(size: Int): Array<Medicacion?> {
            return arrayOfNulls(size)
        }
    }
}