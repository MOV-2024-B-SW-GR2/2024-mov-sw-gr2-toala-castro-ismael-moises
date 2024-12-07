package kotlin.main

object ComputadorCRUD {
    private const val ARCHIVO = "computadores.dat"
    private val computadores: MutableList<Computador> = mutableListOf()

    init {
        cargar()
    }

    private fun cargar() {
        val datos = ArchivoUtil.leerDesdeArchivo<MutableList<Computador>>(ARCHIVO)
        if (datos != null) computadores.addAll(datos)
    }

    private fun guardar() {
        ArchivoUtil.guardarEnArchivo(computadores, ARCHIVO)
    }

    fun crear(computador: Computador) {
        computadores.add(computador)
        guardar()
    }

    fun leer(): List<Computador> = computadores

    fun actualizar(id: Int, nuevoComputador: Computador): Boolean {
        val index = computadores.indexOfFirst { it.id == id }
        return if (index >= 0) {
            computadores[index] = nuevoComputador
            guardar()
            true
        } else false
    }

    fun eliminar(id: Int): Boolean {
        val eliminado = computadores.removeIf { it.id == id }
        if (eliminado) guardar()
        return eliminado
    }
}
