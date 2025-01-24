package main_

import java.time.LocalDate

object ComponenteCRUD {
    private const val rutaArchivo = "computadores.dat"
    private val computadores = ArchivoUtil.cargarDatos<Computador>(rutaArchivo)

    private fun generarIDComponente(): String {
        val letras = (1..3).map { ('A'..'Z').random() }.joinToString("")
        val numeros = (1..3).map { (0..9).random() }.joinToString("")
        return "$letras-$numeros"
    }

    fun agregarComponente() {
        if (computadores.isEmpty()) {
            println("No hay computadores registrados. Agregue un computador primero.")
            return
        }

        println("Seleccione el computador al que desea agregar un componente:")
        val computador = ComputadorCRUD.buscarComputador()
        if (computador != null) {
            try {
                println("Ingrese el nombre del componente:")
                val nombre = readln().takeIf { it.isNotBlank() } ?: throw Exception("Nombre inválido.")

                println("Ingrese el precio del componente:")
                val precio = readln().toDoubleOrNull()?.takeIf { it > 0 } ?: throw Exception("Precio inválido.")

                println("Ingrese el stock del componente:")
                val stock = readln().toIntOrNull()?.takeIf { it >= 0 } ?: throw Exception("Stock inválido.")

                println("¿Está en garantía? (TRUE/T/FALSE/F):")
                val enGarantia = when (readln().uppercase()) {
                    "TRUE", "T" -> true
                    "FALSE", "F" -> false
                    else -> throw Exception("Valor de garantía inválido.")
                }

                println("Ingrese la fecha de fabricación (formato YYYY-MM-DD):")
                val fechaFabricacion = LocalDate.parse(readln())

                val id = generarIDComponente()
                val componente = Componente(id, nombre, precio, stock, enGarantia, fechaFabricacion)
                computador.componentes.add(componente)
                ArchivoUtil.guardarDatos(rutaArchivo, computadores)
                println("Componente agregado exitosamente: $componente")
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        } else {
            println("No se encontró el computador.")
        }
    }


    fun listarComponentes() {
        if (computadores.isEmpty()) {
            println("No hay computadores registrados.")
            return
        }

        println("Seleccione el computador cuyos componentes desea listar:")
        val computador = ComputadorCRUD.buscarComputador()
        if (computador != null) {
            if (computador.componentes.isEmpty()) {
                println("Este computador no tiene componentes registrados.")
            } else {
                computador.componentes.forEach { println(it) }
            }
        } else {
            println("No se encontró el computador.")
        }
    }

    fun eliminarComponente() {
        if (computadores.isEmpty()) {
            println("No hay computadores registrados.")
            return
        }

        println("Seleccione el computador del que desea eliminar un componente:")
        val computador = ComputadorCRUD.buscarComputador()
        if (computador != null) {
            if (computador.componentes.isEmpty()) {
                println("Este computador no tiene componentes registrados.")
                return
            }

            println("Seleccione el componente a eliminar:")
            computador.componentes.forEachIndexed { index, componente ->
                println("${index + 1}. $componente")
            }

            val opcion = readlnOrNull()?.toIntOrNull()?.takeIf { it in 1..computador.componentes.size } ?: run {
                println("Opción inválida. Operación cancelada.")
                return
            }

            val componenteEliminado = computador.componentes.removeAt(opcion - 1)
            ArchivoUtil.guardarDatos(rutaArchivo, computadores)
            println("Componente eliminado exitosamente: $componenteEliminado")
        } else {
            println("No se encontró el computador.")
        }
    }
}
