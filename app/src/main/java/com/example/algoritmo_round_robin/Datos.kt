package com.example.algoritmo_round_robin

import java.io.Serializable

data class Datos(
    val id: Int,                    // Identificador del proceso
    val tiempoLlegada: Int,          // Tiempo de llegada del proceso
    var tiempoRafaga: Int,           // Tiempo de ráfaga actual
    val tiempoRafagaOriginal: Int,   // Tiempo de ráfaga original (nuevo campo)
    var tiempoFinalizacion: Int = 0, // Tiempo en el que el proceso termina
    var tiempoEspera: Int = 0        // Tiempo de espera
) : Serializable
