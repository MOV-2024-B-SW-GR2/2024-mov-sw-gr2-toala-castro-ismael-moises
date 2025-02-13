package com.example.a05_medicationalarm

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

class MDListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaComponente = mutableListOf<Medicacion>()
    private var idComputador = -1
    //Componente
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_uslist_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_list_medicacion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lvl_view_medicacion)
        val txtcomponente = findViewById<TextView>(R.id.txt_view_medicacion)
        val btnAnadirComponente = findViewById<Button>(R.id.btn_crear_medicacion)

        adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, listaComponente.map { it.name })
        listView.adapter = adapter

        val computador = intent.getParcelableExtra<Usuario>("computador")
        if (computador != null) {
            idComputador = computador.id
        }
        txtcomponente.setText("${computador?.name?.toUpperCase(Locale.ROOT)}'S COMPONENTES")
        registerForContextMenu(listView)
        cargarDatosDesdeBaseDeDatos(idComputador)

        btnAnadirComponente.setOnClickListener {
            irActividad(CrudMedicacion::class.java)
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
                irActividad(CrudMedicacion::class.java, componenteSeleccionado)
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
            findViewById(R.id.cl_list_medicacion),
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
                val eliminado = BaseDeDatos.tablaUsuarioMedicamento?.eliminarMedicacion(id)
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

    fun cargarDatosDesdeBaseDeDatos(idUsuario: Int) {
        val medicacion = BaseDeDatos.tablaUsuarioMedicamento?.obtenerMedicacionPorUsuario(idComputador)
        listaComponente.clear()
        if (medicacion != null) {
            listaComponente.addAll(medicacion)

        }
        adapter.clear()
        adapter.addAll(listaComponente.map { it.name })
        adapter.notifyDataSetChanged()
    }

    fun irActividad(clase: Class<*>, medicacion: Medicacion? = null) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("idComputador", idComputador)

        if (medicacion != null) {
            intentExplicito.putExtra("modo", "editar")
            intentExplicito.putExtra("componente", medicacion)
        } else {
            intentExplicito.putExtra("modo", "crear")
        }

        startActivityForResult(intentExplicito, 1)

    }
}