package com.example.a05_medicationalarm

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SqliteHelper(
    context: Context?
): SQLiteOpenHelper(
    context,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = ON;")

        val scriptSQLCrearTablaUsuario =
            """
                CREATE TABLE USUARIO(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    seguro BOOLEAN,
                    edad INTEGER,
                    peso DECIMAL(3, 1),
                    latitude REAL,
                    longitude REAL
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaUsuario)

        val scriptSQLCrearTablaMedicacion =
            """
                CREATE TABLE MEDICACION(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(50),
                    description VARCHAR(50),
                    dosis DECIMAL(3, 1),
                    idUsuario INTEGER,
                    FOREIGN KEY(idUsuario) REFERENCES USUARIO(id) ON DELETE CASCADE
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaMedicacion)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    //Usuario
    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val listaUsuario = mutableListOf<Usuario>()
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM USUARIO"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val seguro = resultadoConsultaLectura.getInt(2) == 1
                val edad = resultadoConsultaLectura.getInt(3)
                val peso = resultadoConsultaLectura.getDouble(5)
                val latitude = resultadoConsultaLectura.getDouble(6)
                val longitude = resultadoConsultaLectura.getDouble(7)


                val usuario = Usuario(
                    id,
                    nombre,
                    edad,
                    seguro,
                    peso,
                    latitude,
                    longitude
                )
                listaUsuario.add(usuario)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaUsuario
    }


    fun crearUsuario(
        nombre: String,
        seguro: Boolean,
        edad:Int,
        peso: Double,
        latitude: Double,
        longitude: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("seguro", seguro)
        valoresGuardar.put("edad",edad)
        valoresGuardar.put("peso", peso)
        valoresGuardar.put("latitude", latitude)
        valoresGuardar.put("longitude", longitude)
        val resultadoGuardar = baseDatosEscritura
            .insert(
                "USUARIO", // nombre tabla
                null,
                valoresGuardar // valores
            )
        baseDatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarUsuario(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase

        val parametrosConsultaDeleteComponente = arrayOf(id.toString())
        baseDatosEscritura.delete("COMPONENTE", "idUsuario=?", parametrosConsultaDeleteComponente)

        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura
            .delete(
                "USUARIO", // tabla
                "id=?", // consulta
                parametrosConsultaDelete // parametros
            )

        baseDatosEscritura.close()
        return if (resultadoEliminar.toInt() == -1) false else true
    }

    fun actualizarUsuario(
        nombre: String, seguro: Boolean, edad:Int,
         peso: Double,
        id: Int, latitude: Double, longitude: Double
    ): Boolean {

        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("seguro", seguro)
        valoresAActualizar.put("edad",edad)
        valoresAActualizar.put("peso", peso)
        valoresAActualizar.put("latitude", latitude)
        valoresAActualizar.put("longitude", longitude)
        val parametrosConsultaUpdate = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura
            .update(
                "USUARIO",
                valoresAActualizar,
                "id=?",
                parametrosConsultaUpdate
            )
        baseDatosEscritura.close()
        return if (resultadoActualizar == -1) false else true
    }

    //Medicacion
    fun obtenerMedicacionPorUsuario(idUsuario: Int): List<Medicacion> {
        val listaComponente = mutableListOf<Medicacion>()
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM COMPONENTE WHERE idUsuario = ?"
        val parametrosConsultaLectura = arrayOf(idUsuario.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val name = resultadoConsultaLectura.getString(1)
                val description = resultadoConsultaLectura.getString(2)
                val tiempo = resultadoConsultaLectura.getString(3)
                val debutfechaString = resultadoConsultaLectura.getString(4)
                val dosis = resultadoConsultaLectura.getDouble(5)

                val fecha = LocalDate.parse(debutfechaString)

                val  medicacion = Medicacion(
                    id,
                    name,
                    description,
                    dosis
                )
                listaComponente.add(medicacion)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaComponente
    }

    fun crearMedicacion(
        name: String,
        description: String,
        dosis: Double,
        idUsuario: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("name", name)
        valoresGuardar.put("description", description)
        valoresGuardar.put("dosis", dosis)
        valoresGuardar.put("idUsuario", idUsuario)

        println("Datos a guardar en MEDICACION: $valoresGuardar")

        val resultadoGuardar = baseDatosEscritura
            .insert(
                "MEDICACION", // nombre tabla
                null,
                valoresGuardar // valores
            )
        baseDatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarMedicacion(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura
            .delete(
                "COMPONENTE", // tabla
                "id=?", // condición
                parametrosConsultaDelete // parámetros
            )
        baseDatosEscritura.close()
        return if (resultadoEliminar.toInt() == -1) false else true
    }

    fun actualizarMedicacion(
        name: String,
        description: String,
        dosis: Double,
        idUsuario: Int,
        id: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("name", name)
        valoresAActualizar.put("description", description)
        valoresAActualizar.put("dosis", dosis)
        valoresAActualizar.put("idComputador", dosis)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura
            .update(
                "MEDICACION", // tabla
                valoresAActualizar, // valores
                "id=?", // condición
                parametrosConsultaActualizar // parámetros
            )
        baseDatosEscritura.close()
        return if (resultadoActualizar == -1) false else true
    }
}