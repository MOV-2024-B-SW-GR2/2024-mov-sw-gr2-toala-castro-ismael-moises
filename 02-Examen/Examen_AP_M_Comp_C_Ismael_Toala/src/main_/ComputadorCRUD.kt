package main_
import java.time.LocalDate

object ComputadorCRUD {
    private const val rutaArchivo = "computadores.dat"
    private val computadores = ArchivoUtil.cargarDatos<Computador>(rutaArchivo)

    private fun generarIDComputador(): String {
        val letras = (1..4).map { ('A'..'Z').random() }.joinToString("")
        val numeros = (1..4).map { (0..9).random() }.joinToString("")
        return "$letras-$numeros"
    }

    fun crearComputador() {
        val id = generarIDComputador()
        println("Creando Computador (ID generado automáticamente: $id)")

        println("Ingrese el nombre del computador:")
        val nombre = readlnOrNull()?.takeIf { it.isNotBlank() } ?: run {
            println("Nombre inválido. Operación cancelada.")
            return
        }

        println("Ingrese la cantidad de RAM (en GB):")
        val ram = readlnOrNull()?.toIntOrNull()?.takeIf { it > 0 } ?: run {
            println("RAM inválida. Operación cancelada.")
            return
        }

        println("¿El computador tiene garantía? (TRUE/T/FALSE/F):")
        val garantia = readlnOrNull()?.uppercase()?.let {
            when (it) {
                "TRUE", "T" -> true
                "FALSE", "F" -> false
                else -> {
                    println("Garantía inválida. Operación cancelada.")
                    return
                }
            }
        } ?: run {
            println("Garantía inválida. Operación cancelada.")
            return
        }

        println("Ingrese la fecha de compra (formato YYYY-MM-DD):")
        val fechaCompra = try {
            LocalDate.parse(readlnOrNull())
        } catch (e: Exception) {
            println("Fecha inválida. Operación cancelada.")
            return
        }

        println("Ingrese el peso del computador en kilogramos:")
        val peso = readlnOrNull()?.toDoubleOrNull()?.takeIf { it > 0 } ?: run {
            println("Peso inválido. Operación cancelada.")
            return
        }

        val computador = Computador(id, nombre, ram, garantia, fechaCompra, peso)
        computadores.add(computador)
        ArchivoUtil.guardarDatos(rutaArchivo, computadores)
        println("Computador creado exitosamente: $computador")
    }

    fun listarComputadores() {
        if (computadores.isEmpty()) {
            println("No hay computadores registrados.")
            return
        }
        computadores.forEach { println(it) }
    }

    fun actualizarComputador() {
        if (computadores.isEmpty()) {
            println("No hay computadores registrados.")
            return
        }

        println("Seleccione el computador a actualizar (por ID o Nombre):")
        val computador = buscarComputador()
        if (computador != null) {
            println("Ingrese el nuevo nombre (dejar vacío para mantener actual):")
            val nuevoNombre = readlnOrNull()?.takeIf { it.isNotBlank() } ?: computador.nombre

            println("Ingrese la nueva cantidad de RAM (dejar vacío para mantener actual):")
            val nuevaRam = readlnOrNull()?.toIntOrNull()?.takeIf { it > 0 } ?: computador.ram

            println("¿Tiene garantía? (TRUE/T/FALSE/F, dejar vacío para mantener actual):")
            val nuevaGarantia = readlnOrNull()?.uppercase()?.let {
                when (it) {
                    "TRUE", "T" -> true
                    "FALSE", "F" -> false
                    else -> computador.garantia
                }
            } ?: computador.garantia

            computador.nombre = nuevoNombre
            computador.ram = nuevaRam
            computador.garantia = nuevaGarantia

            ArchivoUtil.guardarDatos(rutaArchivo, computadores)
            println("Computador actualizado exitosamente: $computador")
        } else {
            println("No se encontró el computador.")
        }
    }

    fun eliminarComputador() {
        if (computadores.isEmpty()) {
            println("No hay computadores registrados.")
            return
        }

        println("Seleccione el computador a eliminar (por ID o Nombre):")
        val computador = buscarComputador()
        if (computador != null) {
            computadores.remove(computador)
            ArchivoUtil.guardarDatos(rutaArchivo, computadores)
            println("Computador eliminado exitosamente.")
        } else {
            println("No se encontró el computador.")
        }
    }

    fun buscarComputador(): Computador? {
        listarComputadores()
        println("Ingrese el criterio de búsqueda (ID o Nombre):")
        val criterio = readlnOrNull()?.lowercase() ?: return null
        return computadores.find { it.id.lowercase() == criterio || it.nombre.lowercase() == criterio }
    }
}
