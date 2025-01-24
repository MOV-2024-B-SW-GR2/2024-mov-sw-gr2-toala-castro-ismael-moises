package main_

import java.io.Serializable
import java.time.LocalDate

data class Computador(
    val id: String,
    var nombre: String,
    var ram: Int,
    var garantia: Boolean,
    var fechaCompra: LocalDate,
    var peso: Double,
    val componentes: MutableList<Componente> = mutableListOf() // Agregada propiedad de componentes
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
