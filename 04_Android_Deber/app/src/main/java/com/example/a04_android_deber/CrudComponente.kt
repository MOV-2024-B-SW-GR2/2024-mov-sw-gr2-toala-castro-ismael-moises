package com.example.a04_android_deber

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

@Suppress("SameParameterValue", "DEPRECATION")
class CrudComponente : AppCompatActivity() {

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_crud_componente),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    private fun validarCampoNoVacio(campo: EditText, mensajeError: String): String? {
        val texto = campo.text.toString().trim()
        if (texto.isEmpty()) {
            mostrarSnackbar(mensajeError)
            return null
        }
        return texto
    }

    private fun validarDouble(campo: EditText, mensajeError: String): Double? {
        val texto = validarCampoNoVacio(campo, mensajeError) ?: return null
        return texto.toDoubleOrNull() ?: run {
            mostrarSnackbar(mensajeError)
            null
        }
    }

    private fun validarInt(campo: EditText, mensajeError: String): Int? {
        val texto = validarCampoNoVacio(campo, mensajeError) ?: return null
        return texto.toIntOrNull() ?: run {
            mostrarSnackbar(mensajeError)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validarFecha(campo: EditText, mensajeError: String): LocalDate? {
        val texto = validarCampoNoVacio(campo, mensajeError) ?: return null
        return try {
            LocalDate.parse(texto)
        } catch (e: Exception) {
            mostrarSnackbar(mensajeError)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_componente)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_crud_componente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val modo = intent.getStringExtra("modo") ?: "crear"
        val componente: Componente? = intent.getParcelableExtra("componente")
        val idComponente = intent.getStringExtra("idComponente") ?: ""

        if (idComponente.isEmpty()) {
            mostrarSnackbar("Error: No se proporcionó un ID válido para el componente.")
            finish()
            return
        }

        val botonGuardarComponenteBDD = findViewById<Button>(R.id.btn_guardar_componente)
        val inputNombre = findViewById<EditText>(R.id.input_name_componente)
        val inputPrecio = findViewById<EditText>(R.id.input_precio)
        val inputStock = findViewById<EditText>(R.id.input_stock)
        val switchEnGarantia = findViewById<Switch>(R.id.switch_en_garantia)
        val inputFechaFabricacion = findViewById<EditText>(R.id.input_fecha_fabricacion)

        if (modo == "editar" && componente != null) {
            inputNombre.setText(componente.nombre)
            inputPrecio.setText(componente.precio.toString())
            inputStock.setText(componente.stock.toString())
            switchEnGarantia.isChecked = componente.enGarantia
            inputFechaFabricacion.setText(componente.fechaFabricacion.toString())
            botonGuardarComponenteBDD.text = "Actualizar"
        } else {
            botonGuardarComponenteBDD.text = "Crear"
        }

        botonGuardarComponenteBDD.setOnClickListener {
            val nombre = validarCampoNoVacio(inputNombre, "El nombre es obligatorio.") ?: return@setOnClickListener
            val precio = validarDouble(inputPrecio, "Ingrese un precio válido.")?.toInt() ?: return@setOnClickListener
            val stock = validarInt(inputStock, "Ingrese un stock válido.") ?: return@setOnClickListener
            val fechaFabricacion = validarFecha(inputFechaFabricacion, "Ingrese una fecha válida.") ?: return@setOnClickListener
            val enGarantia = switchEnGarantia.isChecked

            val respuesta = if (modo == "crear") {
                // Llamada a la función para crear un componente
                BaseDeDatos.tablaComponente?.crearComponente(
                    idComponente, nombre, precio, stock, enGarantia, fechaFabricacion
                )
            } else if (modo == "editar" && componente != null) {
                // Llamada a la función para actualizar un componente
                BaseDeDatos.tablaComponente?.actualizarComponente(
                    idComponente, nombre, precio, stock, enGarantia, fechaFabricacion, componente.id
                )
            } else null

            // Resultado de la operación
            if (respuesta == true) {
                mostrarSnackbar("Componente ${if (modo == "crear") "creado" else "actualizado"} correctamente.")
                setResult(RESULT_OK)
                finish()
            } else {
                mostrarSnackbar("Error al ${if (modo == "crear") "crear" else "actualizar"} el componente.")
            }
        }

    }
}
