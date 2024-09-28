package com.example.algoritmo_round_robin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activity_funcionales_2 : AppCompatActivity() {
    private lateinit var lista_principal: MutableList<Datos>
    private lateinit var lista_terminados: MutableList<Datos>
    private lateinit var lista_pendientes: MutableList<Datos>
    private var quantum = 4 // Valor de quantum, ajustable
    private var tiempoGlobal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionales_2)

        // Recuperar los datos pasados desde la primera actividad
        val lista_principal = intent.getSerializableExtra("listaPrincipal") as MutableList<Datos>
        lista_terminados = mutableListOf()
        lista_pendientes = lista_principal.toMutableList()

        lista_principal.forEachIndexed { index, list ->
            Log.d("MasterList", "Lista acti 2 $index: $list")
        }

        // Inicializar el TextView para mostrar los resultados
        val resultadosTextView = findViewById<TextView>(R.id.resultados_text_view)
        val lineChart = findViewById<LinearLayout>(R.id.line_chart) // Asume que tienes un LinearLayout para mostrar la línea horizontal.

        // Ejecutar el algoritmo de Round Robin
        ejecutarRoundRobin()

        mostrarProgresoDinamico()

        // Mostrar los resultados en un TextView
        mostrarResultados(resultadosTextView)

    }


    private fun ejecutarRoundRobin() {

        try{
        // Ordenar la lista inicial por el tiempo de llegada para asegurar el orden correcto
        lista_pendientes.sortBy { it.tiempoLlegada }
        tiempoGlobal = lista_pendientes[0].tiempoLlegada // Iniciar el tiempo total con el tiempo de llegada del primer proceso
            Log.e("RoundRobinError", "Error en Round Robin: ${lista_pendientes[0]}")
        while (lista_pendientes.isNotEmpty()) {
            val procesoActual = lista_pendientes.removeAt(0) // Obtener el primer proceso

            if (procesoActual.tiempoRafaga <= quantum) {
                // El proceso termina en este quantum
                tiempoGlobal += procesoActual.tiempoRafaga // Incrementar el tiempo total con el tiempo de ráfaga
                procesoActual.tiempoRafaga = 0 // El proceso ya terminó, ráfaga a 0

                // Guardar el tiempo de finalización para este proceso
                procesoActual.tiempoFinalizacion = tiempoGlobal
                lista_terminados.add(procesoActual) // Mover proceso a lista de terminados
            } else {
                // El proceso no termina, reducir el tiempo de ráfaga y volver a añadirlo a pendientes
                tiempoGlobal += quantum
                procesoActual.tiempoRafaga -= quantum
                lista_pendientes.add(procesoActual) // Añadir el proceso nuevamente a la lista de pendientes
            }

            // Si quedan procesos pendientes, actualizar su tiempo de llegada basado en el tiempo total actual
            if (lista_pendientes.isNotEmpty()) {
                val siguienteProceso = lista_pendientes[0]
                if (tiempoGlobal < siguienteProceso.tiempoLlegada) {
                    tiempoGlobal = siguienteProceso.tiempoLlegada // Ajustar el tiempo total si el siguiente proceso no ha llegado aún
                }
            }
        }
        }catch (e: Exception){
            Log.e("RoundRobinError", "Error en Round Robin: ${e.message}")
        }
    }

    private fun mostrarProgresoDinamico() {
        val lineChart = findViewById<LinearLayout>(R.id.line_chart) // Contenedor en el XML para las líneas de proceso

        // Verificar el estado de la lista antes de proceder
        if (lista_terminados.isEmpty()) {
            Log.d("Debug", "La lista de procesos terminados está vacía.")
            return
        }

        // Iterar a través de cada proceso terminado para crear las líneas y etiquetas dinámicamente
        for (proceso in lista_terminados) {
            // Mostrar los valores actuales en el log para depurar
            Log.d("Debug", "Proceso: P${proceso.id}, Tiempo de Finalización: ${proceso.tiempoFinalizacion}")

            // Crear una nueva vista para cada proceso que representa la línea vertical
            val lineView = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    10, // Ancho de la línea (puedes ajustarlo)
                    250 // Alto de la línea
                ).apply {
                    setMargins(20, 0, 20, 0) // Márgenes para alejar las líneas (ajusta según sea necesario)
                }
                setBackgroundColor(Color.BLUE) // Color de la línea (puedes cambiarlo según el diseño)
            }

            // Crear un TextView para mostrar el número del proceso
            val numeroProcesoTextView = TextView(this).apply {
                text = "P${proceso.id}"
                textSize = 14f // Tamaño de texto (puedes ajustarlo)
                setPadding(8, 0, 8, 8) // Espaciado interno del TextView
                gravity = Gravity.CENTER // Centrar el texto horizontalmente
                setTextColor(Color.BLACK) // Cambiar el color para asegurar visibilidad
            }

            // Crear un TextView para mostrar el tiempo de finalización
            val tiempoFinalizacionTextView = TextView(this).apply {
                text = "T. Final: ${proceso.tiempoFinalizacion ?: "N/A"}" // Asegúrate de que el dato no sea nulo
                textSize = 12f // Tamaño de texto (puedes ajustarlo)
                setPadding(8, 8, 8, 0) // Espaciado interno del TextView
                gravity = Gravity.CENTER // Centrar el texto horizontalmente
                setTextColor(Color.RED) // Cambiar el color para asegurar visibilidad
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Cambiar a WRAP_CONTENT
                    LinearLayout.LayoutParams.WRAP_CONTENT // Asegurar que el alto también se ajuste
                )
                // Agregar un log para confirmar que el valor se asigna correctamente
                Log.d("Debug", "Asignando tiempo de finalización: $text")
            }
            // Crear un contenedor para la línea y el texto
            val contenedorProceso = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL // Vista en vertical
                gravity = Gravity.CENTER
                setPadding(20, 0, 20, 0) // Espaciado entre cada contenedor de proceso (ajusta aquí el espaciado entre líneas)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(40, 0, 40, 0) // Márgenes alrededor de cada contenedor de proceso (para alejarlos más)
                }
            }

            // Añadir las vistas al contenedor
            contenedorProceso.addView(numeroProcesoTextView) // Añadir el número del proceso
            contenedorProceso.addView(lineView) // Añadir la línea del proceso
            contenedorProceso.addView(tiempoFinalizacionTextView) // Añadir el tiempo de finalización

            // Añadir el contenedor del proceso al contenedor principal (lineChart)
            lineChart.addView(contenedorProceso)
        }
    }

    private fun mostrarResultados(resultadosTextView : TextView) {

        // Mostrar lista de procesos terminados con el tiempo total acumulado
        val resultadosTerminados = lista_terminados.joinToString(separator = "\n"){ proceso ->
            "Proceso: ${proceso.id}, Llegada: ${proceso.tiempoLlegada}, Tiempo de Finaliazación: ${proceso.tiempoFinalizacion}"
        }

        // Concatenar y mostrar los resultados en el TextView
        resultadosTextView.text = "Procesos Terminados:\n$resultadosTerminados"+ "\nCon un tiempo total de proceso de\n$tiempoGlobal"
    }

}