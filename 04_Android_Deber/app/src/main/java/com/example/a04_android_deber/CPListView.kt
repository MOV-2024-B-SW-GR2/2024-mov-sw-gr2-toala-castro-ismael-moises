package com.example.a04_android_deber

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gr2sw_deber_ksgt.CrudComputador
import com.google.android.material.snackbar.Snackbar


@Suppress("DEPRECATION")
class CPListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaComputadores = mutableListOf<Computador>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cplist_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_list_computador)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lvl_view_computador)
        val botonAnadirListView = findViewById<Button>(R.id.btn_crear_computador)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaComputadores.map { it.nombre })
        listView.adapter = adapter

        registerForContextMenu(listView)

        cargarDatosDesdeBaseDeDatos()

        botonAnadirListView.setOnClickListener {
            irActividad(CrudComputador::class.java) // Pasa el requestCode 1
        }
    }

    private var posicionItemSeleccionado = -1 // VARIABLE GLOBAL
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos() // Refresca la lista
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val computadorSeleccionado = listaComputadores[posicionItemSeleccionado]
                irActividad(CrudComputador::class.java, computadorSeleccionado)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogo()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_list_computador),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            val computadorSeleccionado = listaComputadores[posicionItemSeleccionado]
            val id = computadorSeleccionado.id

            val eliminado = BaseDeDatos.tablaComputador?.eliminarComputador(id)
            if (eliminado == true) {
                mostrarSnackbar("Computador eliminado correctamente.")
                cargarDatosDesdeBaseDeDatos() // Refrescar la lista
            } else {
                mostrarSnackbar("Error al eliminar el computador.")
            }
        }
        builder.setNegativeButton("Cancelar", null)
        val dialogo = builder.create()
        dialogo.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cargarDatosDesdeBaseDeDatos() {
        val computadores = BaseDeDatos.tablaComputador?.obtenerTodosLosComputadores()
        listaComputadores.clear()
        if (computadores != null) {
            listaComputadores.addAll(computadores)
        }
        adapter.clear()
        adapter.addAll(listaComputadores.map { it.nombre })
        adapter.notifyDataSetChanged()
    }

    private fun irActividad(clase: Class<*>, computador: Computador? = null) {
        val intentExplicito = Intent(this, clase)
        if (computador != null) {
            intentExplicito.putExtra("modo", "editar")
            intentExplicito.putExtra("computador", computador)
        } else {
            intentExplicito.putExtra("modo", "crear")
        }
        startActivityForResult(intentExplicito, 1)
    }
}
