package com.example.a05_medicationalarm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrudMedicacion: AppCompatActivity() {
    fun mostrarSnackbar(texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_crud_medicacion),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_medicacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_crud_medicacion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val modo = intent.getStringExtra("modo") ?: "crear"
        val medicacion: Medicacion? = intent.getParcelableExtra("medicacion")
        val idUsuario = intent.getIntExtra("idComputador", -1)

        if (idUsuario == -1) {
            mostrarSnackbar("Error: No se proporcionó un ID válido del computador.")
            finish()
            return
        }

        val botonGuardarComponenteBDD = findViewById<Button>(R.id.btn_guardar_medicacicon)
        val inputNombre = findViewById<EditText>(R.id.input_name_medicacion)
        val inputDescription = findViewById<EditText>(R.id.input_description)
        val inputdosis = findViewById<EditText>(R.id.input_dosis)

        if (modo == "editar" && medicacion != null) {
            // Configurar campos con los datos del superhéroe
            inputNombre.setText(medicacion.name)
            inputDescription.setText(medicacion.description)

            inputdosis.setText(medicacion.dosis.toString())
            botonGuardarComponenteBDD.text = "Actualizar"
        } else {
            botonGuardarComponenteBDD.text = "Crear"
        }

        botonGuardarComponenteBDD.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val description = inputDescription.text.toString().trim()

            val dosisString = inputdosis.text.toString().trim()

            if (nombre.isEmpty() || description.isEmpty() || dosisString.isEmpty()) {
                mostrarSnackbar("Por favor, llene todos los campos")
                return@setOnClickListener
            }

            // Intentar convertir popularidad a número
            val dosis = dosisString.toDoubleOrNull()
            if (dosis == null || dosis < 0) {
                mostrarSnackbar("Por favor, ingresa un valor válido para el precio.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                val respuesta = BaseDeDatos.tablaUsuarioMedicamento?.crearMedicacion(
                    nombre,
                    description,
                    dosis,
                    idUsuario
                )

                if (respuesta == true) {
                    mostrarSnackbar("Poder creado")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el poder")
                }
            } else if (modo == "editar" && medicacion != null) {
                val respuesta = BaseDeDatos.tablaUsuarioMedicamento?.actualizarMedicacion(
                    nombre,
                    description,
                    dosis,
                    idUsuario,
                    medicacion.id
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
