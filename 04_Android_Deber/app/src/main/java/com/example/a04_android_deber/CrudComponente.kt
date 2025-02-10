package com.example.a04_android_deber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrudComponente: AppCompatActivity() {
    fun mostrarSnackbar(texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_crud_componente),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }


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
        val idComputador = intent.getIntExtra("idComputador", -1)

        if (idComputador == -1) {
            mostrarSnackbar("Error: No se proporcionó un ID válido del computador.")
            finish()
            return
        }

        val botonGuardarComponenteBDD = findViewById<Button>(R.id.btn_guardar_componente)
        val inputNombre = findViewById<EditText>(R.id.input_name_componente)
        val inputDescription = findViewById<EditText>(R.id.input_description)
        val switchGarantiaCo = findViewById<Switch>(R.id.switch_garantiaco)
        val inputPrecio = findViewById<EditText>(R.id.input_precio)

        if (modo == "editar" && componente != null) {
            // Configurar campos con los datos del superhéroe
            inputNombre.setText(componente.name)
            inputDescription.setText(componente.description)
            switchGarantiaCo.isChecked = componente.garantiaCo
            inputPrecio.setText(componente.precio.toString())
            botonGuardarComponenteBDD.text = "Actualizar"
        } else {
            botonGuardarComponenteBDD.text = "Crear"
        }

        botonGuardarComponenteBDD.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val description = inputDescription.text.toString().trim()
            val garantiaCo = switchGarantiaCo.isChecked
            val precioString = inputPrecio.text.toString().trim()

            if (nombre.isEmpty() || description.isEmpty() || precioString.isEmpty()) {
                mostrarSnackbar("Por favor, llene todos los campos")
                return@setOnClickListener
            }

            // Intentar convertir popularidad a número
            val precio = precioString.toDoubleOrNull()
            if (precio == null || precio < 0) {
                mostrarSnackbar("Por favor, ingresa un valor válido para el precio.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                val respuesta = BaseDeDatos.tablaComputadorComponente?.crearComponente(
                    nombre,
                    description,
                    garantiaCo,
                    precio,
                    idComputador
                )

                if (respuesta == true) {
                    mostrarSnackbar("Poder creado")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el poder")
                }
            } else if (modo == "editar" && componente != null) {
                val respuesta = BaseDeDatos.tablaComputadorComponente?.actualizarComponente(
                    nombre,
                    description,
                    garantiaCo,
                    precio,
                    idComputador,
                    componente.id
                )

                if (respuesta == true) {
                    mostrarSnackbar("Poder actualizado correctamente")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al actualizar el poder")
                }
            }
        }

    }
}
