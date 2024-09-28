package com.example.algoritmo_round_robin

import java.io.Serializable

data class Datos(val id: Int,
                 val tiempoLlegada: Int,
                 var tiempoRafaga: Int,
                 var tiempoFinalizacion: Int = 0) : Serializable
