package com.example.algoritmo_round_robin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activity_funcionales_2 : AppCompatActivity() {
    private var quantum: Int = 0
    private lateinit var lista_principal: MutableList<Datos>
    private lateinit var lista_terminados: MutableList<Datos>
    private lateinit var lista_pendientes: MutableList<Datos>
    private var tiempoGlobal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionales_2)

        val btn_creditos = findViewById<Button>(R.id.boton_creditos)

        //boton creditos
        btn_creditos.setOnClickListener {
            val intent = Intent(this, activity_creditos::class.java)
            startActivity(intent)
        }

        // Recuperar los datos pasados desde la primera actividad
        lista_principal = intent.getSerializableExtra("listaPrincipal") as MutableList<Datos>
        lista_terminados = mutableListOf()
        lista_pendientes = lista_principal.toMutableList()

        // Recuperar el quantum pasado como extra
        val quantum = intent.getIntExtra("quantum", 4) // Si no se encuentra, toma 4 como valor predeterminado
        this.quantum = quantum // Asignar el quantum al valor recibido

        lista_principal.forEachIndexed { index, list ->
            //Log.d("MasterList", "Lista acti 2 $index: $list")
        }

        //Inicializar el TextView para mostrar los procesos
        val procesoText = findViewById<TextView>(R.id.proceso_text)

        // Inicializar el TextView para mostrar los resultados tabla
        val resultadosTextView = findViewById<TableLayout>(R.id.resultadosTable)


        // Ejecutar el algoritmo de Round Robin
        ejecutarRoundRobin(procesoText)

        mostrarProgresoDinamico()

        // Mostrar los resultados en un TextView
        mostrarResultados(resultadosTextView)

        //Log.d("Acabo2", "Lista de procesos terminados:")
        // Mostrar la lista final de procesos terminados
        lista_terminados.forEach { proceso ->
            Log.d(
                "Acabo2",
                "Proceso ${proceso.id}: Tiempo de Llegada = ${proceso.tiempoLlegada}, " +
                        "Tiempo de Finalización = ${proceso.tiempoFinalizacion}, " +
                        "Tiempo de Espera = ${proceso.tiempoFinalizacion - proceso.tiempoLlegada}"
            )
        }
    }


    private fun ejecutarRoundRobin(proceso_text: TextView) {
        val procesosFinalizados = mutableListOf<Datos>()
        try{

            lista_pendientes.sortBy { it.tiempoLlegada }
            tiempoGlobal = lista_pendientes[0].tiempoLlegada // Iniciar el tiempo total con el tiempo de llegada del primer proceso
            val sb = StringBuilder()

            Log.e("RoundRobinError", "Error en Round Robin: ${lista_pendientes[0]}")
            while (lista_pendientes.isNotEmpty()) {
                val procesoActual = lista_pendientes.removeAt(0) // Obtener el primer proceso
                Log.d("ProcesoActual", "Procesando P${procesoActual.id} - Tiempo de Llegada: ${procesoActual.tiempoLlegada}, Tiempo de Ráfaga Restante: ${procesoActual.tiempoRafaga}")

                // Si el tiempo de ráfaga es menor o igual al quantum, el proceso se completa
                if (procesoActual.tiempoRafaga <= quantum) {
                    tiempoGlobal += procesoActual.tiempoRafaga
                    procesoActual.tiempoFinalizacion = tiempoGlobal

                    // Calcular el tiempo de espera usando el tiempo de ráfaga original
                    val tiempoEspera = procesoActual.tiempoFinalizacion - procesoActual.tiempoLlegada - procesoActual.tiempoRafagaOriginal
                    procesoActual.tiempoEspera = tiempoEspera

                    lista_terminados.add(procesoActual)

                    sb.append("↓\nProceso ${procesoActual.id}: Terminado. Tiempo de Espera: $tiempoEspera\n")
                } else {
                    // Si el proceso no termina en este quantum, restar el quantum a la ráfaga
                    tiempoGlobal += quantum
                    procesoActual.tiempoRafaga -= quantum
                    lista_pendientes.add(procesoActual) // Reagregar el proceso a la lista de pendientes

                    sb.append("↓\nProceso ${procesoActual.id}: Ejecutando... Tiempo de Ráfaga Restante: ${procesoActual.tiempoRafaga}\n")
                }
            }

            // Mostrar el resumen final en el `TextView`
            sb.append("↓\nAlgoritmo completado. Total de procesos: ${lista_terminados.size}\n")
            runOnUiThread {
                proceso_text.text = sb.toString()
            }
        // Primer retraso de 1 segundo para iniciar
        }catch (e: Exception){
            Log.e("RoundRobinError", "Error en Round Robin: ${e.message}")
        }

        // Cuando termines el ciclo
        if (lista_terminados.isNotEmpty()) {
            procesosFinalizados.addAll(lista_terminados)
            Log.d("Finalizados", "Lista de procesos finalizados: $procesosFinalizados")
        } else {
            Log.d("Finalizados", "No hay procesos finalizados.")
        }
    }

    //------------------------------------------------------------------------------------------------------
    private fun mostrarProgresoDinamico() {
        val lineChart = findViewById<LinearLayout>(R.id.line_chart) // Contenedor en el XML para las líneas de proceso
        val lineaHorizontal = findViewById<View>(R.id.linea_horizontal)
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
                text = "T.F: ${proceso.tiempoFinalizacion ?: "N/A"}" // Asegúrate de que el dato no sea nulo
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
                setPadding(10, 0, 10, 0) // Espaciado entre cada contenedor de proceso (ajusta aquí el espaciado entre líneas)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(20, 0, 20, 0) // Márgenes alrededor de cada contenedor de proceso (para alejarlos más)
                }
            }

            // Añadir las vistas al contenedor
            contenedorProceso.addView(numeroProcesoTextView) // Añadir el número del proceso
            contenedorProceso.addView(lineView) // Añadir la línea del proceso


            val lineaHorizontal = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0, // Ancho de la línea (ajusta este valor)
                    5 // Alto de la línea horizontal (ajusta este valor)
                ).apply {
                    weight = 1f // Esto permite que la línea ocupe el espacio restante
                    setMargins(20, 0, 20, 0) // Márgenes para alejar la línea horizontal
                }
                setBackgroundColor(Color.YELLOW) // Color de la línea
            }

            // Añadir la línea horizontal al contenedor
            contenedorProceso.addView(lineaHorizontal)

            contenedorProceso.addView(tiempoFinalizacionTextView) // Añadir el tiempo de finalización

            // Añadir el contenedor del proceso al contenedor principal (lineChart)
            lineChart.addView(contenedorProceso)
        }
        // Ajustar la línea horizontal para que tenga el mismo ancho que el contenedor principal
        lineaHorizontal.layoutParams = lineaHorizontal.layoutParams.apply {
            width = lineChart.width // Asignar el ancho del contenedor
        }
    }

    private fun calcularTiempoFinalizacionPromedio(listaTerminados: List<Datos>): Double {
        val sumaTiempoFinalizacion = listaTerminados.sumOf { it.tiempoFinalizacion }
        return sumaTiempoFinalizacion.toDouble() / listaTerminados.size
    }

    private fun calcularTiempoEsperaPromedio(listaTerminados: List<Datos>, listaOriginal: List<Datos>): Double {
        var sumaTiempoEspera = 0
        listaTerminados.forEach { proceso ->
            val tiempoRafagaOriginal = listaOriginal.first { it.id == proceso.id }.tiempoRafaga
            val tiempoEspera = proceso.tiempoFinalizacion - proceso.tiempoLlegada - tiempoRafagaOriginal
            sumaTiempoEspera += tiempoEspera
        }
        return sumaTiempoEspera.toDouble() / listaTerminados.size
    }

    private fun calcularTiempoEsperaPromedio(listaTerminados: List<Datos>): Double {
        val sumaTiempoEspera = listaTerminados.sumOf { it.tiempoEspera }
        return sumaTiempoEspera.toDouble() / listaTerminados.size
    }

    //------------------------------------------------------------------------------------------------------------------
    private fun mostrarResultados(resultadosTable: TableLayout) {

        val tiempoFinalizacionPromedio = calcularTiempoFinalizacionPromedio(lista_terminados)
        val tiempoEsperaPromedio = calcularTiempoEsperaPromedio(lista_terminados, lista_principal)

        // Mostrar lista de procesos terminados con el tiempo total acumulado
        lista_terminados.forEach { proceso ->
            val row = TableRow(this)

            val tvId = TextView(this).apply {
                text = proceso.id.toString()
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f) // Usar weight para distribuir
                setBackgroundColor(Color.WHITE)
            }

            val tvLlegada = TextView(this).apply {
                text = proceso.tiempoLlegada.toString()
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f) // Usar weight para distribuir
                setBackgroundColor(Color.WHITE)
            }

            val tvEsperaFinal = TextView(this).apply {
                text = proceso.tiempoEspera.toString()
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                setBackgroundColor(Color.WHITE)
            }

            val tvFinalizacion = TextView(this).apply {
                text = proceso.tiempoFinalizacion.toString()
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f) // Usar weight para distribuir
                setBackgroundColor(Color.WHITE)
            }

            row.addView(tvId)
            row.addView(tvLlegada)
            row.addView(tvEsperaFinal)
            row.addView(tvFinalizacion)
            resultadosTable.addView(row)
        }

        // Agregar los tiempos promedio

        // Crear y agregar TextView para el tiempo total
        val tiempoTotalRow = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        }
        val tvTiempoTotal = TextView(this).apply {
            text = "Tiempo total de proceso de: $tiempoGlobal"
            gravity = Gravity.START
            setPadding(8, 8, 8, 8)
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        }
        tiempoTotalRow.addView(tvTiempoTotal)
        resultadosTable.addView(tiempoTotalRow)

// Crear y agregar TextView para el tiempo promedio de finalización
        val tiempoFinalizacionRow = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        }
        val tvTiempoFinalizacionPromedio = TextView(this).apply {
            text = "Tiempo Promedio de Finalización: ${String.format("%.2f", tiempoFinalizacionPromedio)}"
            gravity = Gravity.START
            setPadding(8, 8, 8, 8)
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        }
        tiempoFinalizacionRow.addView(tvTiempoFinalizacionPromedio)
        resultadosTable.addView(tiempoFinalizacionRow)

// Crear y agregar TextView para el tiempo promedio de espera
        val tiempoEsperaRow = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        }
        val tvTiempoEsperaPromedio = TextView(this).apply {
            text = "Tiempo Promedio de Espera: ${String.format("%.2f", tiempoEsperaPromedio)}"
            gravity = Gravity.START
            setPadding(8, 8, 8, 8)
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        }
        tiempoEsperaRow.addView(tvTiempoEsperaPromedio)
        resultadosTable.addView(tiempoEsperaRow)


    }

}