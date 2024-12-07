package kotlin.main
import java.io.Serializable

data class Computador(
    var id: Int,
    var nombre: String,
    var fechaCompra: String, // Fecha
    var precio: Double,      // Decimal
    var tieneGarantia: Boolean, // Booleano
    var componentes: MutableList<Componente> = mutableListOf() // Relación uno a muchos
) : Serializable
