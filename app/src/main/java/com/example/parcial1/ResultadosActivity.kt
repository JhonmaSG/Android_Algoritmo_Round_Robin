package com.example.parcial1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadosActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        // Log para verificar si onCreate se está llamando
        Log.d("ResultadosActivity", "onCreate called")

        // Recibir los datos enviados desde la actividad anterior
        val quantum = intent.getIntExtra("quantum", -1)  // Valor por defecto -1 si no se recibe
        val rafagas = intent.getIntegerArrayListExtra("rafagas")
        val ordenesLlegada = intent.getIntegerArrayListExtra("ordenesLlegada")

        // Validar si los datos han sido recibidos correctamente
        if (quantum == -1 || rafagas == null || ordenesLlegada == null) {
            Log.e("ResultadosActivity", "Datos no recibidos correctamente")
            return  // No continúes si los datos no están bien
        }

        // Log para verificar los datos recibidos
        Log.d("ResultadosActivity", "Quantum: $quantum, Rafagas: $rafagas, OrdenesLlegada: $ordenesLlegada")

        val btnMostrarResultados = findViewById<Button>(R.id.btnMostrarResultados)

        btnMostrarResultados.setOnClickListener {
            // Calcular Round Robin y mostrar el resultado en un popup
            val resultados = calcularRoundRobin(quantum, rafagas, ordenesLlegada)
            mostrarPopup(resultados)
        }
    }

   public fun calcularRoundRobin(quantum: Int, rafagas: List<Int>, ordenesLlegada: List<Int>): String {
        val numProcesos = rafagas.size
        val tiemposEspera = IntArray(numProcesos)
        val tiemposFinalizacion = IntArray(numProcesos)
        val tiemposRestantes = rafagas.toMutableList()

        var tiempoTotal = 0
        var procesosRestantes = numProcesos
        val resultados = StringBuilder()

        // Listado de procesos con su orden de llegada
        resultados.append("Carga de procesos:\n")
        for (i in ordenesLlegada.indices) {
            resultados.append("Proceso ${i + 1} (Llegada: ${ordenesLlegada[i]}, Ráfaga: ${rafagas[i]})\n")
        }

        resultados.append("\nAsignación de tiempos de ejecución:\n")

        // Simulación del algoritmo Round Robin
        while (procesosRestantes > 0) {
            for (i in 0 until numProcesos) {
                if (tiemposRestantes[i] > 0) {
                    if (tiemposRestantes[i] > quantum) {
                        tiempoTotal += quantum
                        tiemposRestantes[i] -= quantum
                        resultados.append("Proceso ${i + 1} ejecutado de t=${tiempoTotal - quantum} a t=$tiempoTotal\n")
                    } else {
                        tiempoTotal += tiemposRestantes[i]
                        tiemposFinalizacion[i] = tiempoTotal
                        tiemposEspera[i] = tiempoTotal - rafagas[i]
                        resultados.append("Proceso ${i + 1} finaliza en t=$tiempoTotal\n")
                        tiemposRestantes[i] = 0
                        procesosRestantes--
                    }
                }
            }
        }

        // Cálculo del tiempo promedio de espera y de finalización
        val tiempoPromedioEspera = tiemposEspera.sum().toDouble() / numProcesos
        val tiempoPromedioFinalizacion = tiemposFinalizacion.sum().toDouble() / numProcesos

        resultados.append("\nCálculo de tiempos:\n")
        for (i in 0 until numProcesos) {
            resultados.append("Proceso ${i + 1}: Tiempo de Espera = ${tiemposEspera[i]}, Tiempo de Finalización = ${tiemposFinalizacion[i]}\n")
        }

        resultados.append("\nPromedios:\n")
        resultados.append("Tiempo promedio de espera: $tiempoPromedioEspera\n")
        resultados.append("Tiempo promedio de finalización: $tiempoPromedioFinalizacion\n")

        return resultados.toString()
    }

    @SuppressLint("MissingInflatedId")
    private fun mostrarPopup(resultados: String) {
        val popupView = layoutInflater.inflate(R.layout.popup_resultados, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Mostrar resultados en el TextView del popup
        val tvResultadosDetalle = popupView.findViewById<TextView>(R.id.tvResultadosDetalle)
        tvResultadosDetalle.text = resultados

        val btnCerrar = popupView.findViewById<Button>(R.id.btnCerrar)
        btnCerrar.setOnClickListener {
            popupWindow.dismiss()  // Cerrar el popup cuando se presiona el botón
        }

        // Mostrar el popup en el centro de la pantalla
        popupWindow.showAtLocation(findViewById(R.id.resultadosLayout), Gravity.CENTER, 0, 0)
    }
}
