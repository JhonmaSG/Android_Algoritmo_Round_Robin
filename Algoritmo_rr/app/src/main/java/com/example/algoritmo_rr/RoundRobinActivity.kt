package com.example.roundrobinapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.algoritmo_rr.R

data class Proceso(val nombre: String, var tiempoRafaga: Int, var tiempoEspera: Int = 0, var tiempoFinalizacion: Int = 0)

class RoundRobinActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_robin)

        val procesos = mutableListOf(
            Proceso("P1", 10),
            Proceso("P2", 5),
            Proceso("P3", 8)
        )

        val quantum = 4
        val resultado = ejecutarRoundRobin(procesos, quantum)

        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        tvResultado.text = resultado
    }

    private fun ejecutarRoundRobin(procesos: MutableList<Proceso>, quantum: Int): String {
        var tiempo = 0
        var colaProcesos = procesos.toMutableList()

        while (colaProcesos.isNotEmpty()) {
            val procesoActual = colaProcesos.removeAt(0)
            if (procesoActual.tiempoRafaga > quantum) {
                tiempo += quantum
                procesoActual.tiempoRafaga -= quantum
                colaProcesos.add(procesoActual)
            } else {
                tiempo += procesoActual.tiempoRafaga
                procesoActual.tiempoFinalizacion = tiempo
            }
        }

        val tiemposPromedio = calcularTiemposPromedio(procesos)
        return generarResultado(procesos, tiemposPromedio)
    }

    private fun calcularTiemposPromedio(procesos: List<Proceso>): Pair<Double, Double> {
        val totalEspera = procesos.sumBy { it.tiempoEspera }
        val totalFinalizacion = procesos.sumBy { it.tiempoFinalizacion }
        val promedioEspera = totalEspera.toDouble() / procesos.size
        val promedioFinalizacion = totalFinalizacion.toDouble() / procesos.size
        return Pair(promedioEspera, promedioFinalizacion)
    }

    private fun generarResultado(procesos: List<Proceso>, tiemposPromedio: Pair<Double, Double>): String {
        val sb = StringBuilder()
        sb.append("Proceso | Tiempo Finalización | Tiempo Espera\n")
        procesos.forEach {
            sb.append("${it.nombre} | ${it.tiempoFinalizacion} | ${it.tiempoEspera}\n")
        }
        sb.append("\nTiempo Promedio de Espera: ${tiemposPromedio.first}")
        sb.append("\nTiempo Promedio de Finalización: ${tiemposPromedio.second}")
        return sb.toString()
    }
}
