package com.example.a04_android_deber

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

@Suppress("DEPRECATION")
class COListView: AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaComponente = mutableListOf<Componente>()
    private var idComputador = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_colist_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_list_componente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lvl_view_componente)
        val txtPower = findViewById<TextView>(R.id.txt_view_componente)
        val btnAnadirPower = findViewById<Button>(R.id.btn_crear_componente)

        adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, listaComponente.map { it.nombre })
        listView.adapter = adapter

        val computador = intent.getParcelableExtra<Computador>("computador")
        if (computador != null) {
            idComputador = computador.id
        }
        txtPower.text = "${computador?.nombre?.toUpperCase(Locale.ROOT)}'S Componentes"
        registerForContextMenu(listView)
        cargarDatosDesdeBaseDeDatos(idComputador)

        btnAnadirPower.setOnClickListener {
            irActividad(CrudComponente::class.java)
        }
    }

    private var posicionItemSeleccionado = -1 // VARIABLE GLOBAL
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // llenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.mis_componentes)?.isVisible = false
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos(idComputador) // Refresca la lista
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onContextItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val powerSeleccionado = listaComponente[posicionItemSeleccionado]
                irActividad(CrudComponente::class.java, powerSeleccionado)
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }

            R.id.mis_componentes -> {
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_list_componente),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar este componente?")
        builder.setPositiveButton(
            "Aceptar"
        ) { _, _ -> // Eliminar el componente seleccionado
            val componenteSeleccionado = listaComponente[posicionItemSeleccionado]
            val id = componenteSeleccionado.id

            // Llamar al método de eliminación de la base de datos
            val eliminado = BaseDeDatos.tablaComponente?.eliminarComponente(id)
            if (eliminado == true) {
                mostrarSnackbar("Componente eliminado correctamente.")
                cargarDatosDesdeBaseDeDatos(idComputador) // Refrescar la lista de componentes
            } else {
                mostrarSnackbar("Error al eliminar el componente.")
            }
        }
        builder.setNegativeButton("Cancelar", null)
        val dialogo = builder.create()
        dialogo.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cargarDatosDesdeBaseDeDatos(idComputador: String) {
        // Obtener los componentes asociados al computador desde la base de datos
        val componentes = BaseDeDatos.tablaComponente?.obtenerComponentesPorComputador(idComputador)
        listaComponente.clear()
        if (componentes != null) {
            listaComponente.addAll(componentes)
        }
        // Actualizar el adaptador con los nombres de los componentes
        adapter.clear()
        adapter.addAll(listaComponente.map { it.nombre })
        adapter.notifyDataSetChanged()
    }


    private fun irActividad(clase: Class<*>, componente: Componente? = null) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("idSuperhero", idComputador)

        if (componente != null) {
            intentExplicito.putExtra("modo", "editar")
            intentExplicito.putExtra("componente", componente)
        } else {
            intentExplicito.putExtra("modo", "crear")
        }

        startActivityForResult(intentExplicito, 1)

    }
}