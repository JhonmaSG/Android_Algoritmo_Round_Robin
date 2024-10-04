package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class activity_funcionales_1 : AppCompatActivity() {
    private lateinit var lista_actual: MutableList<Datos>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_funcionales_1)
        // Acá se debe realizar un ejemplo funcional del algoritmo Round Robin donde se evidencie
        // - todo el proceso desde la carga de los procesos:
        // - asignación de tiempos de ráfaga,
        // - linea de tiempo
        // - calculo del tiempo de espera
        // - finalización de cada proceso,
        // - tiempo promedio de espera
        // - tiempo promedio de finalización
        // Inicializar la lista principal y la sublista actual.
        lista_actual = mutableListOf()

        // Referenciar los elementos de la interfaz (suponiendo que tienes dos EditText y un Button)
        val numero_quantum = findViewById<EditText>(R.id.quantum)
        val numero_proceso = findViewById<EditText>(R.id.numero_proceso)
        val tiempo_rafaga = findViewById<EditText>(R.id.tiempo_rafaga)
        val tiempo_llegada = findViewById<EditText>(R.id.tiempo_llegada)
        val anadir_datos = findViewById<Button>(R.id.anadir_proceso)
        val ver_procesos = findViewById<Button>(R.id.ver_procesos)
        val ver_datos = findViewById<TextView>(R.id.ver_datos)


        // Configurar el botón para agregar datos a la sublista actual
        anadir_datos.setOnClickListener {
            val numero_procesos = numero_proceso.text.toString()
            var tiempo_rafagas = tiempo_rafaga.text.toString()
            var tiempo_llegadas = tiempo_llegada.text.toString()

            val duplicados = esElementoDuplicado(numero_procesos,tiempo_llegadas)
            val proceso_duplicado = duplicados.first
            val tiempo_llegada_duplicado = duplicados.second

            if (numero_procesos.isNotEmpty() && tiempo_rafagas.isNotEmpty() && tiempo_llegadas.isNotEmpty()) {
                if (!proceso_duplicado) {
                    if(!tiempo_llegada_duplicado){
                        // Agregar la entrada de datos a la sublista actual
                        lista_actual.add(Datos(
                            id = numero_procesos.toInt(),
                            tiempoLlegada = tiempo_llegadas.toInt(),
                            tiempoRafaga = tiempo_rafagas.toInt(),
                            tiempoRafagaOriginal = tiempo_rafagas.toInt()  // Pasamos el valor original de tiempo de ráfaga
                        ))

                        // Limpiar los EditTexts para agregar más datos
                        numero_proceso.text.clear()
                        tiempo_rafaga.text.clear()
                        tiempo_llegada.text.clear()

                        // Mostrar todos los datos en el TextView
                        mostrarDatosEnTextView(ver_datos)

                        // Mostrar la lista actual en el log o la consola para verificar (opcional)
                        lista_actual.forEachIndexed { index, list ->
                            Log.d("MasterList", "Lista $index: $list")
                        }
                    }else{
                        // Mostrar mensaje de error si el tiempo de llegada ya existe
                        Toast.makeText(this, "El tiempo de llegada $tiempo_llegadas ya existe . Por favor, ingrese otro tiempo.", Toast.LENGTH_SHORT).show()
                    }


            }else {
                    // Mostrar mensaje de error si el número de proceso ya existe
                    Toast.makeText(this, "El número de proceso $numero_procesos ya existe . Por favor, ingrese otro número.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }

        }



        ver_procesos.setOnClickListener {
            // Verificar que el campo de quantum no esté vacío
            val quantum = numero_quantum.text.toString()

            if (quantum.isNotEmpty()) {
                // Crear un Intent para ir a activity_funcionales_2
                val intent = Intent(this, activity_funcionales_2::class.java)
                intent.putExtra("listaPrincipal", lista_actual as Serializable) // Pasar lista_principal como un extra
                intent.putExtra("quantum", quantum.toInt()) // Pasar el quantum ingresado como extra

                startActivity(intent) // Iniciar la nueva actividad
            } else {
                Toast.makeText(this, "Por favor, ingrese un valor para el quantum.", Toast.LENGTH_SHORT).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    // Función para mostrar los datos en el TextView
    private fun mostrarDatosEnTextView(verDatos: TextView) {
        val stringBuilder = StringBuilder()

        for (element in lista_actual) {
            stringBuilder.append("Proceso: ${element.id}\n")
            stringBuilder.append("Tiempo de Llegada: ${element.tiempoLlegada}\n")
            stringBuilder.append("Tiempo de Ráfaga: ${element.tiempoRafaga}\n")
            stringBuilder.append("\n") // Agregar espacio entre elementos
        }

        verDatos.text = stringBuilder.toString()
    }

    private fun esElementoDuplicado(idProceso: String, tiempoLlegada: String): Pair<Boolean,Boolean> {
        var respuesta1 = false
        var respuesta2 = false
        for (element in lista_actual) {
            if (element.id == idProceso.toInt()) {
                respuesta1 = true // Elemento duplicado
            }
            if(element.tiempoLlegada == tiempoLlegada.toInt()){
                respuesta2 = true
            }
        }
        return Pair(respuesta1,respuesta2) // No hay elementos duplicados
    }
}