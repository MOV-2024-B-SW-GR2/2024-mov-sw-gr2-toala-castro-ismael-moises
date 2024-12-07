package main_
import java.io.*

object ArchivoUtil {
    fun guardarDatos(ruta: String, data: Any) {
        try {
            File(ruta).outputStream().use { file ->
                ObjectOutputStream(file).use { it.writeObject(data) }
            }
        } catch (e: Exception) {
            println("Error al guardar los datos: ${e.message}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> cargarDatos(ruta: String): MutableList<T> {
        return try {
            if (File(ruta).exists()) {
                File(ruta).inputStream().use { file ->
                    ObjectInputStream(file).use { it.readObject() as MutableList<T> }
                }
            } else mutableListOf()
        } catch (e: Exception) {
            println("Error al cargar los datos: ${e.message}")
            mutableListOf()
        }
    }
}

