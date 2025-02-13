package com.example.a05_medicationalarm

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrudUsuario : AppCompatActivity() {
    fun mostrarSnackbar(texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_crud_usuario),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_crud_usuario)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val modo = intent.getStringExtra("modo") ?: "crear"
        val usuario: Usuario? = intent.getParcelableExtra("usuario")
        val botonGuardarComputadorBDD = findViewById<Button>(R.id.btn_guardar_usuario)
        val inputNombre = findViewById<EditText>(R.id.input_name_usuario)
        val inputedad = findViewById<EditText>(R.id.input_edad)
        val switchSeguro = findViewById<Switch>(R.id.switch_en_seguro)
        val inputPeso = findViewById<EditText>(R.id.input_peso)
        val inputLatitude = findViewById<EditText>(R.id.txt_latitude)
        val inputLongitude = findViewById<EditText>(R.id.txt_longitude)

        if (modo == "editar" && usuario != null) {
            // Configurar campos con los datos del superhéroe
            inputNombre.setText(usuario.name)
            switchSeguro.isChecked = usuario.seguro
            inputedad.setText(usuario.edad)
            inputPeso.setText(usuario.peso.toString())
            inputLatitude.setText(usuario.latitude.toString())
            inputLongitude.setText(usuario.longitude.toString())
            botonGuardarComputadorBDD.text = "Actualizar"
        } else {
            botonGuardarComputadorBDD.text = "Crear"
        }

        botonGuardarComputadorBDD.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val seguro = switchSeguro.isChecked
            val edadString = inputedad.text.toString().trim()
            val pesoString = inputPeso.text.toString().trim()
            val latitudeString = inputLatitude.text.toString().trim()
            val longitudeString = inputLongitude.text.toString().trim()



            // Intentar convertir RAM a número
            val edad = edadString.toIntOrNull()
            if (edad == null || edad < 0) {
                mostrarSnackbar("Por favor, ingresa un valor válido para la RAM.")
                return@setOnClickListener
            }

            // Intentar convertir peso a número
            val peso = pesoString.toDoubleOrNull()
            val latitude = latitudeString.toDoubleOrNull()
            val longitude = longitudeString.toDoubleOrNull()
            if (peso == null || peso < 0) {
                mostrarSnackbar("Por favor, ingresa un valor válido para el peso.")
                return@setOnClickListener
            }
            if(latitude == null || longitude == null){
                mostrarSnackbar("Por favor, ingresa valores válidos para la latitud y longitud.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                val respuesta = BaseDeDatos.tablaUsuarioMedicamento?.crearUsuario(
                    nombre,
                    seguro,
                    edad,
                    peso,
                    latitude,
                    longitude
                )

                if (respuesta == true) {
                    mostrarSnackbar("Computador creado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el Computador.")
                }
            } else if (modo == "editar" && usuario != null) {
                val respuesta = BaseDeDatos.tablaUsuarioMedicamento?.actualizarUsuario(
                    nombre,
                    seguro,
                    edad,
                    peso,
                    usuario.id,
                    latitude,
                    longitude,
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