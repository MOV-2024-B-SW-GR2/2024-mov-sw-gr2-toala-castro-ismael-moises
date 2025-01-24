package org.example

import java.util.*

fun main(args: Array<String>) {
    val inmutable: String ="Adrian";
    var mutable: String = "Vicente";
    mutable= "Adrian";
    //Kotlin es fuertemente tipado
    //Duck typing
    val ejemploVariable="Jonathan Poaquiza"
    ejemploVariable.trim()
    val edadEjemplo:Int=12

    val nombreProfesor:String="Jonathan Poaquiza";
    val sueldo:Double=1.2
    val estadoCivil:Char='C'
    val mayorEdad:Boolean=true

    //clases en java
    val fechaNacimiento: Date= Date()
    //en KOTLIN tenemos acceso a todas las clases y tipos de datos de java
    //string, char, int, boolean y date
    //when-es como switch
    val estadoCivilWhen='C'
    when(estadoCivilWhen){
        ('C')->{
            println("Casado")
        }
        'S'->{
            println("Soltero")
        }
        else->{
            println("No sabemos")
        }
    }
    val esSoltero=(estadoCivilWhen=='S')
    val coqueteo=if(esSoltero) "Si" else "No" //no se usa el operador ternario, se usa if else de una linea
    //cada bloque puede tener o no parentesis y empieza con flecha

    val sumaA = Suma(1,1)
    val sumaB = Suma(null,1)
    val sumaC = Suma(1,null)
    val sumaD = Suma(null,null)
    sumaA.sumar()
    sumaA.sumar()
    sumaA.sumar()
    sumaA.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    imprimirNombre("Jonathan")
    calcularSueldo(10.00)//solo el requerido
    calcularSueldo(10.00,15.00,20.00)//requerido y opcionales
    //Named parameters
    calcularSueldo(sueldo=10.00,bonoEspecial=20.00)// bono en 2do lugar
    //gracias a los parametros nombrados
    calcularSueldo(bonoEspecial=20.00,sueldo=10.00, tasa = 14.00)// bono en 1er lugar
}


fun imprimirNombre(nombre:String):Unit{//no existe el void pero si el Unit, es la funcion que no devueove nada
fun otraFuncionAdentro(){ //funcion dentro de otra funcion
    println("Otra funcion adentro")//no es requerido poner unit
    //kotlin entiende que no devuelve nada
}
    println("Nombre: $nombre")//template strings
    println("Nombre: ${nombre}")//se puede hacer operaciones dentro
    println("Nombre: ${nombre+nombre}")//llaves opcionales en ciertos casos
    println("Nombre: ${nombre.toString()}")
    println("Nombre: $nombre.toString()")
    otraFuncionAdentro()

}

fun calcularSueldo(
    sueldo:Double, //parametro requerido
    tasa:Double = 12.00, //parametro opcional
    bonoEspecial:Double? = null //parametro opcional que puede ser nulo
    //si no se le pasa un valor, se le asigna null, ?->es nulable
):Double{
    //Int->Int? Nulable
    //String->String? Nulable
    //Date->Date? Nulable
    if(bonoEspecial==null){
        return sueldo*(100/tasa)
    }else{
        return sueldo*(100/tasa)+bonoEspecial
    }
}

abstract class NumerosJava{
    protected val numeroUno:Int
    private val numeroDos:Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno=uno
        this.numeroDos=dos
        println("Inicializando")
    }
}
abstract class Numeros(//constructor primario ojo-parentesis() para cosntructor primario
    protected val numeroUno:Int,//instancia.numeroUno
    val numeroDos:Int//instancia.numeroDos
    //parametroNoUsadoNoPropiedadDeLaClase:Int?=null
){
    init {//BLOQUE DE CODIGO DEL CONSTRUCTOR PRIMARIO
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

class Suma(
    unoParametro:Int,
    dosParametro:Int
):Numeros(//constructor primario con clase padre
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicito: String = "Publico implicito"
    init {
        this.numeroUno
        this.numeroDos
        numeroUno
        numeroDos
        this.soyPublicoImplicito
        soyPublicoExplicito
    }
    constructor(
        uno: Int?,
        dos: Int
    ):this(
        if(uno==null) 0 else uno,
        dos
    )
    constructor(
        uno: Int,
        dos: Int?
    ):this(
        uno,
        if(dos==null) 0 else dos
    )
    constructor(
        uno: Int?,
        dos: Int?
    ):this(
        if(uno==null) 0 else uno,
        if(dos==null) 0 else dos
    )
    fun sumar():Int{
        val total = numeroUno+numeroDos
        agregarHistorial(total)
        return total
    }
    companion object{
        val pi = 3.14
        fun elevarAlCuadrado(num:Int):Int{return num * num}
        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial(ValorTotalSuma:Int){
            historialSumas.add(ValorTotalSuma)
        }
    }
}