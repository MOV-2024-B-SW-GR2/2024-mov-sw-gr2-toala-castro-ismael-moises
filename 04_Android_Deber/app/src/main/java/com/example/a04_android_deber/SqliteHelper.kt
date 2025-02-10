package com.example.a04_android_deber

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

        val scriptSQLCrearTablaComputador =
            """
                CREATE TABLE COMPUTADOR(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    garantia BOOLEAN,
                    ram INTEGER,
                    debutCompra TEXT,
                    peso DECIMAL(3, 1),
                    latitude REAL,
                    longitude REAL
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaComputador)

        val scriptSQLCrearTablaComponente =
            """
                CREATE TABLE COMPONENTE(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(50),
                    description VARCHAR(50),
                    garantiaCo BOOLEAN,
                    precio DECIMAL(3, 1),
                    idComputador INTEGER,
                    FOREIGN KEY(idComputador) REFERENCES COMPUTADOR(id) ON DELETE CASCADE
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaComponente)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    //Computador
    fun obtenerTodosLosComputadores(): List<Computador> {
        val listaComputador = mutableListOf<Computador>()
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM COMPUTADOR"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val garantia = resultadoConsultaLectura.getInt(2) == 1
                val ram = resultadoConsultaLectura.getInt(3)
                val debutCompraString = resultadoConsultaLectura.getString(4)
                val peso = resultadoConsultaLectura.getDouble(5)
                val latitude = resultadoConsultaLectura.getDouble(6)
                val longitude = resultadoConsultaLectura.getDouble(7)

                val debutCompra = LocalDate.parse(debutCompraString)

                val computador = Computador(
                    id,
                    nombre,
                    ram,
                    garantia,
                    debutCompra,
                    peso,
                    latitude,
                    longitude
                )
                listaComputador.add(computador)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaComputador
    }


    fun crearComputador(
        nombre: String,
        garantia: Boolean,
        ram:Int,
        debutCompra: LocalDate,
        peso: Double,
        latitude: Double,
        longitude: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("garantia", garantia)
        valoresGuardar.put("ram",ram)
        valoresGuardar.put("debutCompra", debutCompra.format(DateTimeFormatter.ISO_DATE))
        valoresGuardar.put("peso", peso)
        valoresGuardar.put("latitude", latitude)
        valoresGuardar.put("longitude", longitude)
        val resultadoGuardar = baseDatosEscritura
            .insert(
                "COMPUTADOR", // nombre tabla
                null,
                valoresGuardar // valores
            )
        baseDatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarComputador(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase

        val parametrosConsultaDeleteComponente = arrayOf(id.toString())
        baseDatosEscritura.delete("COMPONENTE", "idComputador=?", parametrosConsultaDeleteComponente)

        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura
            .delete(
                "COMPUTADOR", // tabla
                "id=?", // consulta
                parametrosConsultaDelete // parametros
            )

        baseDatosEscritura.close()
        return if (resultadoEliminar.toInt() == -1) false else true
    }

    fun actualizarComputador(
        nombre: String, garantia: Boolean, ram:Int,
        debutCompra: LocalDate, peso: Double,
        id: Int, latitude: Double, longitude: Double
    ): Boolean {

        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("garantia", garantia)
        valoresAActualizar.put("ram",ram)
        valoresAActualizar.put("debutCompra", debutCompra.format(DateTimeFormatter.ISO_DATE))
        valoresAActualizar.put("peso", peso)
        valoresAActualizar.put("latitude", latitude)
        valoresAActualizar.put("longitude", longitude)
        val parametrosConsultaUpdate = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura
            .update(
                "COMPUTADOR",
                valoresAActualizar,
                "id=?",
                parametrosConsultaUpdate
            )
        baseDatosEscritura.close()
        return if (resultadoActualizar == -1) false else true
    }

    //Componente
    fun obtenerComponentesPorComputador(idComputador: Int): List<Componente> {
        val listaComponente = mutableListOf<Componente>()
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM COMPONENTE WHERE idComputador = ?"
        val parametrosConsultaLectura = arrayOf(idComputador.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val name = resultadoConsultaLectura.getString(1)
                val description = resultadoConsultaLectura.getString(2)
                val garantiaCo = resultadoConsultaLectura.getInt(3) == 1
                val precio = resultadoConsultaLectura.getDouble(4)

                val componente = Componente(
                    id,
                    name,
                    description,
                    garantiaCo,
                    precio
                )
                listaComponente.add(componente)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaComponente
    }

    fun crearComponente(
        name: String,
        description: String,
        garantiaCo: Boolean,
        precio: Double,
        idComputador: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("name", name)
        valoresGuardar.put("description", description)
        valoresGuardar.put("garantiaCo", garantiaCo)
        valoresGuardar.put("precio", precio)
        valoresGuardar.put("idComputador", idComputador)

        println("Datos a guardar en COMPONENTE: $valoresGuardar")

        val resultadoGuardar = baseDatosEscritura
            .insert(
                "COMPONENTE", // nombre tabla
                null,
                valoresGuardar // valores
            )
        baseDatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarComponente(id: Int): Boolean {
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

    fun actualizarComponente(
        name: String,
        description: String,
        garantiaCo: Boolean,
        precio: Double,
        idComputador: Int,
        id: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("name", name)
        valoresAActualizar.put("description", description)
        valoresAActualizar.put("garantiaCo", garantiaCo)
        valoresAActualizar.put("precio", precio)
        valoresAActualizar.put("idComputador", idComputador)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura
            .update(
                "COMPONENTE", // tabla
                valoresAActualizar, // valores
                "id=?", // condición
                parametrosConsultaActualizar // parámetros
            )
        baseDatosEscritura.close()
        return if (resultadoActualizar == -1) false else true
    }
}