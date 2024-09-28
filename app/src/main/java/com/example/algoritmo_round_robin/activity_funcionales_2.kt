package com.example.algoritmo_round_robin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activity_funcionales_2 : AppCompatActivity() {
    private lateinit var lista_principal: MutableList<MutableList<String>>
    private lateinit var lista_terminados: MutableList<MutableList<String>>
    private lateinit var lista_pendientes: MutableList<MutableList<String>>
    private var quantum = 4 // Valor de quantum, ajustable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionales_2)


        lista_principal.forEachIndexed { index, list ->
            Log.d("MasterList", "Lista acti 2 $index: $list")
        }
        // Recuperar los datos pasados desde la primera actividad
        val listaPrincipal :  MutableList<MutableList<String>>? = intent.getSerializableExtra("listaPrincipal") as? MutableList<MutableList<String>>
        lista_terminados = mutableListOf()
        lista_pendientes = mutableListOf()

        // Inicializar el TextView para mostrar los resultados
        val resultadosTextView = findViewById<TextView>(R.id.resultados_text_view)
        val lineChart = findViewById<LinearLayout>(R.id.line_chart) // Asume que tienes un LinearLayout para mostrar la línea horizontal.

        // Iterar sobre cada proceso en lista_principal y aplicar Round Robin
        while (lista_principal.isNotEmpty()) {
            val iterador = lista_principal.iterator()
            while (iterador.hasNext()) {
                val proceso = iterador.next()
                val numeroProceso = proceso[0]
                val tiempoRafaga = proceso[1].toInt()
                val tiempoLlegada = proceso[2].toInt()

                if (tiempoRafaga <= quantum) {
                    // Proceso termina, se guarda el tiempo total (tiempo de llegada + ráfaga)
                    val tiempoTotal = tiempoLlegada + tiempoRafaga
                    val procesoTerminado = mutableListOf(numeroProceso, tiempoLlegada.toString(), tiempoTotal.toString())
                    lista_terminados.add(procesoTerminado)


                    // Remover el proceso de la lista principal
                    iterador.remove()
                } else {
                    // Proceso no termina, actualizar tiempo de ráfaga y mover a pendientes
                    val nuevoTiempoRafaga = tiempoRafaga - quantum
                    val procesoPendiente = mutableListOf(numeroProceso, nuevoTiempoRafaga.toString(), tiempoLlegada.toString())
                    lista_pendientes.add(procesoPendiente)

                    // Remover el proceso de la lista principal y agregarlo a pendientes
                    iterador.remove()
                }
            }

            // Pasar los pendientes a la lista principal para la siguiente ronda
            lista_principal.addAll(lista_pendientes)
            lista_pendientes.clear()
        }

        // Mostrar los resultados finales en el TextView
        val resultados = StringBuilder()
        lista_terminados.forEach { proceso ->
            resultados.append("Proceso ${proceso[0]}: Llegada = ${proceso[1]}, Tiempo Total = ${proceso[2]}\n")
        }
        resultadosTextView.text = resultados.toString()
    }

}