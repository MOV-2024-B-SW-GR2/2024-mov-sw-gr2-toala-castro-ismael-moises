package main_
import java.io.Serializable
import java.time.LocalDate

data class Componente(
    val id: String,
    var nombre: String,
    var precio: Double,
    var stock: Int,
    var enGarantia: Boolean,
    var fechaFabricacion: LocalDate
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

