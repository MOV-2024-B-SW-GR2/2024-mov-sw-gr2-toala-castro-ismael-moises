package com.example.a04_android_deber
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
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CrudComputador : AppCompatActivity() {
    fun mostrarSnackbar(texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_crud_computador),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
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
        val inputNombre = findViewById<EditText>(R.id.input_name_computador)
        val inputRam = findViewById<EditText>(R.id.input_ram_computador)
        val switchActivo = findViewById<Switch>(R.id.switch_en_garantia)
        val inputFecha = findViewById<EditText>(R.id.input_date)
        val inputPeso = findViewById<EditText>(R.id.input_peso)
        val inputLatitude = findViewById<EditText>(R.id.txt_latitude)
        val inputLongitude = findViewById<EditText>(R.id.txt_longitude)

        if (modo == "editar" && computador != null) {
            // Configurar campos con los datos del superhéroe
            inputNombre.setText(computador.name)
            switchActivo.isChecked = computador.garantia
            inputRam.setText(computador.ram)
            inputFecha.setText(computador.debutCompra.toString())
            inputPeso.setText(computador.peso.toString())
            inputLatitude.setText(computador.latitude.toString())
            inputLongitude.setText(computador.longitude.toString())
            botonGuardarComputadorBDD.text = "Actualizar"
        } else {
            botonGuardarComputadorBDD.text = "Crear"
        }

        botonGuardarComputadorBDD.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val garantia = switchActivo.isChecked
            val ramString = inputRam.text.toString().trim()
            val debutDateString = inputFecha.text.toString().trim()
            val pesoString = inputPeso.text.toString().trim()
            val latitudeString = inputLatitude.text.toString().trim()
            val longitudeString = inputLongitude.text.toString().trim()

            // Validar entradas
            if (nombre.isEmpty() || debutDateString.isEmpty() || pesoString.isEmpty()) {
                mostrarSnackbar("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            // Convertir fecha a LocalDate
            val debutCompra: LocalDate = try {
                LocalDate.parse(debutDateString)
            } catch (e: DateTimeParseException) {
                mostrarSnackbar("Por favor, ingresa una fecha válida en el formato AAAA-MM-DD.")
                return@setOnClickListener
            }

            // Intentar convertir RAM a número
            val ram = ramString.toIntOrNull()
            if (ram == null || ram < 0) {
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
                val respuesta = BaseDeDatos.tablaComputadorComponente?.crearComputador(
                    nombre,
                    garantia,
                    ram,
                    debutCompra,
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
            } else if (modo == "editar" && computador != null) {
                val respuesta = BaseDeDatos.tablaComputadorComponente?.actualizarComputador(
                    nombre,
                    garantia,
                    ram,
                    debutCompra,
                    peso,
                    computador.id,
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