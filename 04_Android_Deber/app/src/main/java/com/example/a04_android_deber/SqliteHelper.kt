package com.example.a04_android_deber

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class SqliteHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "computadora_componente.db"
        private const val DB_VERSION = 1
        private const val TABLE_COMPUTADOR = "computador"
        private const val TABLE_COMPONENTE = "componente"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createComputadorTable = """
            CREATE TABLE $TABLE_COMPUTADOR (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                ram INTEGER NOT NULL,
                garantia INTEGER NOT NULL,
                fechaCompra TEXT NOT NULL,
                peso REAL NOT NULL
            );
        """.trimIndent()
        db?.execSQL(createComputadorTable)

        val createComponenteTable = """
            CREATE TABLE $TABLE_COMPONENTE (
                id TEXT PRIMARY KEY,
                nombre TEXT NOT NULL,
                precio REAL NOT NULL,
                stock INTEGER NOT NULL,
                enGarantia INTEGER NOT NULL,
                fechaFabricacion TEXT NOT NULL
            );
        """.trimIndent()
        db?.execSQL(createComponenteTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COMPUTADOR")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COMPONENTE")
        onCreate(db)
    }

    // CRUD para Computador
    fun crearComputador(nombre: String, ram: Int, garantia: Boolean, fechaCompra: LocalDate, peso: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("ram", ram)
            put("garantia", if (garantia) 1 else 0)
            put("fechaCompra", fechaCompra.toString())
            put("peso", peso)
        }
        val result = db.insert(TABLE_COMPUTADOR, null, values)
        db.close()
        return result != -1L
    }

    fun actualizarComputador(id: String, nombre: String, ram: Int, garantia: Boolean, fechaCompra: LocalDate, peso: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("ram", ram)
            put("garantia", if (garantia) 1 else 0)
            put("fechaCompra", fechaCompra.toString())
            put("peso", peso)
        }
        val result = db.update(TABLE_COMPUTADOR, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
    fun eliminarComputador(id: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_COMPUTADOR, "id = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun obtenerComputador(id: Int): Computador? {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_COMPUTADOR, null, "id = ?", arrayOf(id.toString()), null, null, null)

        return if (cursor.moveToFirst()) {
            val computador = Computador(
                id.toString(),
                cursor.getString(cursor.getColumnIndex("nombre")),
                cursor.getInt(cursor.getColumnIndex("ram")),
                cursor.getInt(cursor.getColumnIndex("garantia")) == 1,
                LocalDate.parse(cursor.getString(cursor.getColumnIndex("fechaCompra"))),
                cursor.getDouble(cursor.getColumnIndex("peso"))
            )
            cursor.close()
            db.close()
            computador
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerTodosLosComputadores(): List<Computador> {
        val listaComputadores = mutableListOf<Computador>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_COMPUTADOR"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val ram = cursor.getInt(cursor.getColumnIndexOrThrow("ram"))
                val garantia = cursor.getInt(cursor.getColumnIndexOrThrow("garantia")) == 1
                val fechaCompra = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaCompra")))
                val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))

                val computador = Computador(id.toString(), nombre, ram, garantia, fechaCompra, peso)
                listaComputadores.add(computador)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaComputadores
    }




    // CRUD para Componente
    fun crearComponente(
        id: String,
        nombre: String,
        precio: Int?,
        stock: Int,
        enGarantia: Boolean,
        fechaFabricacion: LocalDate
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("precio", precio)
            put("stock", stock)
            put("enGarantia", if (enGarantia) 1 else 0) // Guardar enGarantia como 1 o 0
            put("fechaFabricacion", fechaFabricacion.toString()) // Convertir LocalDate a String
        }
        val result = db.insert(TABLE_COMPONENTE, null, values)
        db.close()
        return result != -1L
    }


    fun actualizarComponente(
        id: String,
        nombre: String,
        precio: Int?,
        stock: Int,
        enGarantia: Boolean,
        fechaFabricacion: LocalDate,
        id1: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            put("stock", stock)
            put("enGarantia", if (enGarantia) 1 else 0) // Guardar enGarantia como 1 o 0
            put("fechaFabricacion", fechaFabricacion.toString()) // Convertir LocalDate a String
        }
        val result = db.update(TABLE_COMPONENTE, values, "id = ?", arrayOf(id))
        db.close()
        return result > 0
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun obtenerComponente(id: String): Componente? {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_COMPONENTE, null, "id = ?", arrayOf(id), null, null, null)

        return if (cursor.moveToFirst()) {
            val componente = Componente(
                id,
                cursor.getString(cursor.getColumnIndex("nombre")),
                cursor.getDouble(cursor.getColumnIndex("precio")),
                cursor.getInt(cursor.getColumnIndex("stock")),
                cursor.getInt(cursor.getColumnIndex("enGarantia")) == 1,
                LocalDate.parse(cursor.getString(cursor.getColumnIndex("fechaFabricacion")))
            )
            cursor.close()
            db.close()
            componente
        } else {
            cursor.close()
            db.close()
            null
        }
    }
    // Función para obtener componentes por computador
    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerComponentesPorComputador(computadorId: String): List<Componente> {
        val listaComponentes = mutableListOf<Componente>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_COMPONENTE WHERE id IN (SELECT componente_id FROM relacion_computador_componente WHERE computador_id = ?)"
        val cursor = db.rawQuery(query, arrayOf(computadorId))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                val stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
                val enGarantia = cursor.getInt(cursor.getColumnIndexOrThrow("enGarantia")) == 1
                val fechaFabricacion = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaFabricacion")))

                val componente = Componente(id, nombre, precio, stock, enGarantia, fechaFabricacion)
                listaComponentes.add(componente)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaComponentes
    }

    // Función para eliminar un componente
    fun eliminarComponente(id: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_COMPONENTE, "id = ?", arrayOf(id))
        db.close()
        return result > 0
    }

}
