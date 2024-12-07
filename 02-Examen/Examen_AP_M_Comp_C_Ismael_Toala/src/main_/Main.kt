package main_

fun main() {
    while (true) {
        println("\nMenú Principal:")
        println("1. Crear Computador")
        println("2. Listar Computadores")
        println("3. Actualizar Computador")
        println("4. Eliminar Computador")
        println("5. Agregar Componente a Computador")
        println("6. Listar Componentes de un Computador")
        println("7. Eliminar Componente de un Computador")
        println("8. Salir")
        println("Escoja la opcion que este de acuerdo a su necesidad: ")
        when (readlnOrNull()) {
            "1" -> ComputadorCRUD.crearComputador()
            "2" -> ComputadorCRUD.listarComputadores()
            "3" -> ComputadorCRUD.actualizarComputador()
            "4" -> ComputadorCRUD.eliminarComputador()
            "5" -> ComponenteCRUD.agregarComponente()
            "6" -> ComponenteCRUD.listarComponentes()
            "7" -> ComponenteCRUD.eliminarComponente()
            "8" -> return
            else -> println("Opción inválida. Intente de nuevo.")
        }
    }
}

