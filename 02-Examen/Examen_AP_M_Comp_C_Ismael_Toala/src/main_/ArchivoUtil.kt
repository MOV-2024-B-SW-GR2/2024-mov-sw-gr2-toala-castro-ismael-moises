package kotlin.main
import java.io.*

object ArchivoUtil {
    fun <T> guardarEnArchivo(objeto: T, archivo: String) {
        ObjectOutputStream(FileOutputStream(archivo)).use { it.writeObject(objeto) }
    }

    fun <T> leerDesdeArchivo(archivo: String): T? {
        return if (File(archivo).exists()) {
            ObjectInputStream(FileInputStream(archivo)).use { it.readObject() as T }
        } else null
    }
}
