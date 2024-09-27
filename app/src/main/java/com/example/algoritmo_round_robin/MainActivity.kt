package com.example.algoritmo_round_robin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.LinkedList
import java.util.Queue

class MainActivity : AppCompatActivity() {

    private lateinit var inputProcessCount: EditText
    private lateinit var inputBurstTimes: EditText
    private lateinit var inputQuantum: EditText
    private lateinit var btnSimulate: Button
    private lateinit var resultOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*Bienvenida al tutorial del Algoritmo de Planificación*/

        inputProcessCount = findViewById(R.id.input_process_count)
        inputBurstTimes = findViewById(R.id.input_burst_times)
        inputQuantum = findViewById(R.id.input_quantum)
        btnSimulate = findViewById(R.id.btn_simulate)
        resultOutput = findViewById(R.id.result_output)

        // Configurar el botón para ejecutar la simulación
        btnSimulate.setOnClickListener {
            simulateRoundRobin()
        }
    }

    // Función para simular el algoritmo de Round Robin
    private fun simulateRoundRobin() {
        // Obtener los datos de la interfaz
        val processCount = inputProcessCount.text.toString().toInt()
        val burstTimesString = inputBurstTimes.text.toString().split(",")
        val quantum = inputQuantum.text.toString().toInt()

        // Convertir los tiempos de burst a enteros
        val burstTimes = IntArray(processCount) { burstTimesString[it].trim().toInt() }

        // Variables para la simulación
        val remainingTimes = burstTimes.clone()
        var time = 0
        val processQueue: Queue<Int> = LinkedList()
        val result = StringBuilder()

        // Añadir los procesos a la cola inicialmente
        for (i in 0 until processCount) {
            processQueue.add(i)
        }

        // Simular Round Robin
        while (processQueue.isNotEmpty()) {
            val currentProcess = processQueue.poll()

            if (remainingTimes[currentProcess] > quantum) {
                time += quantum
                remainingTimes[currentProcess] -= quantum
                processQueue.add(currentProcess) // Volver a poner en cola
            } else {
                time += remainingTimes[currentProcess]
                remainingTimes[currentProcess] = 0
                result.append("Proceso ${currentProcess + 1} completado en ")
                    .append("$time unidades de tiempo.\n")
            }
        }

        // Mostrar el resultado en el TextView
        resultOutput.text = result.toString()
    }

}