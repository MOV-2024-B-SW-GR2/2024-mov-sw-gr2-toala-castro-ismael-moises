package kotlin.main

fun main() {
    while (true) {
        println("\n=== Menú Principal ===")
        println("1. Crear Computador")
        println("2. Listar Computadores")
        println("3. Actualizar Computador")
        println("4. Eliminar Computador")
        println("5. Agregar Componente a Computador")
        println("6. Eliminar Componente de Computador")
        println("0. Salir")
        print("Seleccione una opción: ")
        when (readLine()?.toIntOrNull()) {
            1 -> crearComputador()
            2 -> listarComputadores()
            3 -> actualizarComputador()
            4 -> eliminarComputador()
            5 -> agregarComponente()
            6 -> eliminarComponente()
            0 -> {
                println("Saliendo del programa...")
                break
            }
            else -> println("Opción inválida, intente nuevamente.")
        }
    }
}

fun crearComputador() {
    println("\n=== Crear Computador ===")
    print("ID: ")
    val id = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    print("Nombre: ")
    val nombre = readLine().orEmpty()
    print("Fecha de compra (YYYY-MM-DD): ")
    val fechaCompra = readLine().orEmpty()
    print("Precio: ")
    val precio = readLine()?.toDoubleOrNull() ?: return println("Precio inválido.")
    print("¿Tiene garantía? (true/false): ")
    val tieneGarantia = readLine()?.toBooleanStrictOrNull() ?: return println("Valor inválido para garantía.")

    val computador = Computador(id, nombre, fechaCompra, precio, tieneGarantia)
    ComputadorCRUD.crear(computador)
    println("Computador creado exitosamente.")
}

fun listarComputadores() {
    println("\n=== Lista de Computadores ===")
    val computadores = ComputadorCRUD.leer()
    if (computadores.isEmpty()) {
        println("No hay computadores registrados.")
    } else {
        computadores.forEach { computador ->
            println(computador)
        }
    }
}

fun actualizarComputador() {
    println("\n=== Actualizar Computador ===")
    print("ID del computador a actualizar: ")
    val id = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    val computadorExistente = ComputadorCRUD.leer().find { it.id == id }
    if (computadorExistente == null) {
        println("No se encontró un computador con el ID proporcionado.")
        return
    }

    print("Nuevo Nombre: ")
    val nombre = readLine().orEmpty()
    print("Nueva Fecha de compra (YYYY-MM-DD): ")
    val fechaCompra = readLine().orEmpty()
    print("Nuevo Precio: ")
    val precio = readLine()?.toDoubleOrNull() ?: return println("Precio inválido.")
    print("¿Tiene garantía? (true/false): ")
    val tieneGarantia = readLine()?.toBooleanStrictOrNull() ?: return println("Valor inválido para garantía.")

    val nuevoComputador = Computador(id, nombre, fechaCompra, precio, tieneGarantia, computadorExistente.componentes)
    if (ComputadorCRUD.actualizar(id, nuevoComputador)) {
        println("Computador actualizado exitosamente.")
    } else {
        println("Error al actualizar el computador.")
    }
}

fun eliminarComputador() {
    println("\n=== Eliminar Computador ===")
    print("ID del computador a eliminar: ")
    val id = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    if (ComputadorCRUD.eliminar(id)) {
        println("Computador eliminado exitosamente.")
    } else {
        println("No se encontró un computador con el ID proporcionado.")
    }
}

fun agregarComponente() {
    println("\n=== Agregar Componente ===")
    print("ID del computador: ")
    val idComputador = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    val computador = ComputadorCRUD.leer().find { it.id == idComputador }
    if (computador == null) {
        println("No se encontró un computador con el ID proporcionado.")
        return
    }

    print("ID del componente: ")
    val idComponente = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    print("Nombre del componente: ")
    val nombre = readLine().orEmpty()
    print("Tipo: ")
    val tipo = readLine().orEmpty()
    print("Cantidad: ")
    val cantidad = readLine()?.toIntOrNull() ?: return println("Cantidad inválida.")
    print("Precio Unitario: ")
    val precioUnitario = readLine()?.toDoubleOrNull() ?: return println("Precio inválido.")

    val componente = Componente(idComponente, nombre, tipo, cantidad, precioUnitario)
    if (ComponenteCRUD.agregarComponente(idComputador, componente)) {
        println("Componente agregado exitosamente.")
    } else {
        println("Error al agregar el componente.")
    }
}

fun eliminarComponente() {
    println("\n=== Eliminar Componente ===")
    print("ID del computador: ")
    val idComputador = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    print("ID del componente: ")
    val idComponente = readLine()?.toIntOrNull() ?: return println("ID inválido.")
    if (ComponenteCRUD.eliminarComponente(idComputador, idComponente)) {
        println("Componente eliminado exitosamente.")
    } else {
        println("No se encontró el componente o el computador con los IDs proporcionados.")
    }
}
