package com.example.gr2sw_deber_ksgt

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
import com.example.a04_android_deber.Computador
import com.example.a04_android_deber.R
import com.example.a04_android_deber.BaseDeDatos
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CrudComputador : AppCompatActivity() {
    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_crud_computador),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_computador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_crud_computador)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val modo = intent.getStringExtra("modo") ?: "crear"
        val computador: Computador? = intent.getParcelableExtra("computador")

        val botonGuardarComputadorBDD = findViewById<Button>(R.id.btn_guardar_computador)
        val inputNombre = findViewById<EditText>(R.id.input_nombre_computador)
        val inputRam = findViewById<EditText>(R.id.input_ram_computador)
        val switchGarantia = findViewById<Switch>(R.id.switch_garantia_computador)
        val inputFechaCompra = findViewById<EditText>(R.id.input_fecha_compra_computador)
        val inputPeso = findViewById<EditText>(R.id.input_peso_computador)

        if (modo == "editar" && computador != null) {
            // Configurar campos con los datos del computador
            inputNombre.setText(computador.nombre)
            inputRam.setText(computador.ram.toString())
            switchGarantia.isChecked = computador.garantia
            inputFechaCompra.setText(computador.fechaCompra.toString())
            inputPeso.setText(computador.peso.toString())
            botonGuardarComputadorBDD.text = "Actualizar"
        } else {
            botonGuardarComputadorBDD.text = "Crear"
        }

        botonGuardarComputadorBDD.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val ramString = inputRam.text.toString().trim()
            val garantia = switchGarantia.isChecked
            val fechaCompraString = inputFechaCompra.text.toString().trim()
            val pesoString = inputPeso.text.toString().trim()

            // Validar entradas
            if (nombre.isEmpty() || ramString.isEmpty() || fechaCompraString.isEmpty() || pesoString.isEmpty()) {
                mostrarSnackbar("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            // Validar RAM
            val ram = ramString.toIntOrNull()
            if (ram == null || ram <= 0) {
                mostrarSnackbar("Por favor, ingresa un valor válido para la RAM.")
                return@setOnClickListener
            }

            // Validar fecha de compra
            val fechaCompra: LocalDate = try {
                LocalDate.parse(fechaCompraString)
            } catch (e: DateTimeParseException) {
                mostrarSnackbar("Por favor, ingresa una fecha válida en el formato AAAA-MM-DD.")
                return@setOnClickListener
            }

            // Validar peso
            val peso = pesoString.toDoubleOrNull()
            if (peso == null || peso <= 0) {
                mostrarSnackbar("Por favor, ingresa un valor válido para el peso.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                val respuesta = BaseDeDatos.tablaComputador?.crearComputador(
                    nombre,
                    ram,
                    garantia,
                    fechaCompra,
                    peso
                )

                if (respuesta == true) {
                    mostrarSnackbar("Computador creado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el computador.")
                }
            } else if (modo == "editar" && computador != null) {
                val respuesta = BaseDeDatos.tablaComputador?.actualizarComputador(
                    computador.id,
                    nombre,
                    ram,
                    garantia,
                    fechaCompra,
                    peso
                )

                if (respuesta == true) {
                    mostrarSnackbar("Computador actualizado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al actualizar el computador.")
                }
            }
        }
    }
}
