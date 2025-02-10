package com.example.a04_android_deber

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class COListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaComponente = mutableListOf<Componente>()
    private var idComputador = -1
    //Componente
    @SuppressLint("SetTextI18n")
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
        val txtcomponente = findViewById<TextView>(R.id.txt_view_componente)
        val btnAnadirComponente = findViewById<Button>(R.id.btn_crear_componente)

        adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, listaComponente.map { it.name })
        listView.adapter = adapter

        val computador = intent.getParcelableExtra<Computador>("computador")
        if (computador != null) {
            idComputador = computador.id
        }
        txtcomponente.setText("${computador?.name?.toUpperCase(Locale.ROOT)}'S COMPONENTES")
        registerForContextMenu(listView)
        cargarDatosDesdeBaseDeDatos(idComputador)

        btnAnadirComponente.setOnClickListener {
            irActividad(CrudComponente::class.java)
        }
    }

    var posicionItemSeleccionado = -1 // VARIABLE GLOBAL
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
        menu?.findItem(R.id.route_mapa)?.isVisible = false
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos(idComputador) // Refresca la lista
        }
    }

    override fun onContextItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val componenteSeleccionado = listaComponente[posicionItemSeleccionado]
                irActividad(CrudComponente::class.java, componenteSeleccionado)
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }

            R.id.mis_componentes-> {
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_list_componente),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Cerrar") { snack.dismiss() }
        snack.show()
    }

    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{ dialog, which ->
                val componenteSeleccionado = listaComponente[posicionItemSeleccionado]
                val id = componenteSeleccionado.id

                // Llamar al método de eliminación
                val eliminado = BaseDeDatos.tablaComputadorComponente?.eliminarComponente(id)
                if (eliminado == true) {
                    mostrarSnackbar("Componente eliminado correctamente.")
                    cargarDatosDesdeBaseDeDatos(idComputador) // Refrescar la lista
                } else {
                    mostrarSnackbar("Error al eliminar el componente.")
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

    fun cargarDatosDesdeBaseDeDatos(idComputador: Int) {
        val componentes = BaseDeDatos.tablaComputadorComponente?.obtenerComponentesPorComputador(idComputador)
        listaComponente.clear()
        if (componentes != null) {
            listaComponente.addAll(componentes)

        }
        adapter.clear()
        adapter.addAll(listaComponente.map { it.name })
        adapter.notifyDataSetChanged()
    }

    fun irActividad(clase: Class<*>, componente: Componente? = null) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("idComputador", idComputador)

        if (componente != null) {
            intentExplicito.putExtra("modo", "editar")
            intentExplicito.putExtra("componente", componente)
        } else {
            intentExplicito.putExtra("modo", "crear")
        }

        startActivityForResult(intentExplicito, 1)

    }
}