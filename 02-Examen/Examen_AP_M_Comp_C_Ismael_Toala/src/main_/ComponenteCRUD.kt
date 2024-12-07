package kotlin.main

object ComponenteCRUD {
    fun agregarComponente(idComputador: Int, componente: Componente): Boolean {
        val computador = ComputadorCRUD.leer().find { it.id == idComputador }
        return if (computador != null) {
            computador.componentes.add(componente)
            ComputadorCRUD.actualizar(computador.id, computador)
            true
        } else false
    }

    fun eliminarComponente(idComputador: Int, idComponente: Int): Boolean {
        val computador = ComputadorCRUD.leer().find { it.id == idComputador }
        return if (computador != null) {
            val eliminado = computador.componentes.removeIf { it.id == idComponente }
            if (eliminado) ComputadorCRUD.actualizar(computador.id, computador)
            eliminado
        } else false
    }
}
