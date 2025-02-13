package com.example.a05_medicationalarm

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class USListView: AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaComputador = mutableListOf<Usuario>()
    //Computador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_uslist_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_list_usuario)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lvl_view_computador)
        val botonAnadirListView = findViewById<Button>(R.id.btn_crear_computador)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaComputador.map { it.name })
        listView.adapter = adapter

        registerForContextMenu(listView)

        cargarDatosDesdeBaseDeDatos()

        botonAnadirListView.setOnClickListener {
            irActividad(CrudUsuario::class.java) // Pasa el requestCode 1
        }


    }

    var posicionItemSeleccionado = -1 // VARIABLE GLOBAL
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu, v, menuInfo)
        // llenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        // obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos() // Refresca la lista
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val computadorSeleccionado = listaComputador[posicionItemSeleccionado]
                irActividad(CrudUsuario::class.java, computadorSeleccionado)
                return true
            }
            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }
            R.id.mis_componentes -> {
                val computadorSeleccionado = listaComputador[posicionItemSeleccionado]
                irActividad(MDListView::class.java, computadorSeleccionado)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_list_usuario),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{ dialog, which ->
                val computadorSeleccionado = listaComputador[posicionItemSeleccionado]
                val id = computadorSeleccionado.id

                // Llamar al método de eliminación
                val eliminado = BaseDeDatos.tablaUsuarioMedicamento?.eliminarUsuario(id)
                if (eliminado == true) {
                    mostrarSnackbar("Computador eliminado correctamente.")
                    cargarDatosDesdeBaseDeDatos() // Refrescar la lista
                } else {
                    mostrarSnackbar("Error al eliminar el computador.")
                }
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )
        val dialogo = builder.create()
        dialogo.show()
    }

    fun cargarDatosDesdeBaseDeDatos() {
        val usuario = BaseDeDatos.tablaUsuarioMedicamento?.obtenerTodosLosUsuarios()
        listaComputador.clear()
        if (usuario != null) {
            listaComputador.addAll(usuario)

        }
        adapter.clear()
        adapter.addAll(listaComputador.map { it.name })
        adapter.notifyDataSetChanged()
    }

    fun irActividad(clase: Class<*>, usuario: Usuario? = null) {
        val intentExplicito = Intent(this, clase)
        if (usuario != null) {
            intentExplicito.putExtra("modo", "editar")
            intentExplicito.putExtra("usuario", usuario)
        } else {
            intentExplicito.putExtra("modo", "crear")
        }
        startActivityForResult(intentExplicito, 1)
    }
}