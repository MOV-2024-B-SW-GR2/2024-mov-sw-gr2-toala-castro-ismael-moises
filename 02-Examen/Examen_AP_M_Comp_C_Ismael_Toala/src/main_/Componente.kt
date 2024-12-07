package kotlin.main

import java.io.Serializable

data class Componente(
    var id: Int,
    var nombre: String,
    var tipo: String,   // String
    var cantidad: Int,  // Entero
    var precioUnitario: Double // Decimal
) : Serializable
