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

class activity_funcionales_1 : AppCompatActivity() {
    private lateinit var lista_principal: MutableList<MutableList<String>>
    private lateinit var lista_actual: MutableList<String>
    private lateinit var textViewDatos: TextView

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
        lista_principal = mutableListOf()
        lista_actual = mutableListOf()

        // Referenciar los elementos de la interfaz (suponiendo que tienes dos EditText y un Button)
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

            if (numero_procesos.isNotEmpty() && tiempo_rafagas.isNotEmpty() && tiempo_llegadas.isNotEmpty()) {
                if (!esNumeroProcesoDuplicado(numero_procesos)) {
                // Agregar la entrada de datos a la sublista actual
                lista_actual.add(numero_procesos)
                lista_actual.add(tiempo_rafagas)
                lista_actual.add(tiempo_llegadas)

                // Limpiar los EditTexts para agregar más datos
                numero_proceso.text.clear()
                tiempo_rafaga.text.clear()
                tiempo_llegada.text.clear()
            }else {
                    // Mostrar mensaje de error si el número de proceso ya existe
                    Toast.makeText(this, "El número de proceso $numero_procesos ya existe. Por favor, ingrese otro número.", Toast.LENGTH_SHORT).show()
                }
            }
            if (lista_actual.isNotEmpty()) {
                // Agregar la sublista actual a la lista principal
                lista_principal.add(lista_actual)


                // Mostrar la lista actual en el log o la consola para verificar (opcional)
                lista_principal.forEachIndexed { index, list ->
                    Log.d("MasterList", "Lista $index: $list")
                }

                // Mostrar todos los datos en el TextView
                mostrarDatosEnTextView(ver_datos)

                // Crear una nueva sublista vacía para seguir agregando datos
                lista_actual = mutableListOf()


            }
        }



        ver_procesos.setOnClickListener {
            // Convertir lista_principal en ArrayList<ArrayList<String>> para que sea serializable
            val listaSerializable = lista_principal.map { ArrayList(it) } as ArrayList<ArrayList<String>>

            // Crear un Intent para ir a activity_funcionales_2
            val intent = Intent(this, activity_funcionales_2::class.java).apply{
                intent.putExtra("lista_principal", listaSerializable) // Pasar lista_principal como un extra
            }


            startActivity(intent) // Iniciar la nueva actividad

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    // Función para mostrar los datos en el TextView
    private fun mostrarDatosEnTextView(ver_datos: TextView) {
        val stringBuilder = StringBuilder()

        // Recorrer la lista principal y concatenar los datos
        for (subLista in lista_principal) {
            // Asumiendo que cada sublista tiene 3 elementos
            stringBuilder.append("Proceso: ${subLista[0]}\n")
            stringBuilder.append("Tiempo de Llegada: ${subLista[1]}\n")
            stringBuilder.append("Tiempo de Ráfaga: ${subLista[2]}\n")
            stringBuilder.append("\n") // Agregar espacio entre procesos
        }

        // Asigna el texto al TextView
        ver_datos.text = stringBuilder.toString()
    }

    // Función para verificar si el número de proceso ya existe en lista_principal
    private fun esNumeroProcesoDuplicado(numero_proceso: String): Boolean {
        // Recorrer la lista principal y verificar si el número de proceso ya existe
        for (subLista in lista_principal) {
            if (subLista.isNotEmpty() && subLista[0] == numero_proceso) {
                return true // Número de proceso duplicado
            }
        }
        return false // No hay duplicados
    }
}