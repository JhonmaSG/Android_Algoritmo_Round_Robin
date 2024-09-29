package com.example.algoritmo_round_robin

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class activityFuncionales1 : AppCompatActivity() {
    // Variables globales
    private lateinit var etQuantum: EditText
    private lateinit var etNombreProceso: EditText
    private lateinit var etTiempoLlegada: EditText
    private lateinit var etRafaga: EditText
    private lateinit var spinnerColorProceso: Spinner
    private lateinit var btnAnadir: Button

    private val listaProcesos = mutableListOf<Proceso>()

    //Funcion para agregar los procesos a recycleView


    // Función para validar si los campos están completos
    private fun validarCampos(): Boolean {
        return when {
            etNombreProceso.text.isEmpty() -> false
            etQuantum.text.isEmpty() -> false
            etTiempoLlegada.text.isEmpty() -> false
            etRafaga.text.isEmpty() -> false
            else -> true
        }
    }

    // Función para limpiar los campos después de agregar el proceso
    private fun limpiarCampos() {
        etNombreProceso.text.clear()
        etQuantum.text.clear()
        etTiempoLlegada.text.clear()
        etRafaga.text.clear()
    }

    // Función para obtener el color basado en la selección del Spinner
    private fun obtenerColorSeleccionado(colorSeleccionado: String): Int {
        return when (colorSeleccionado) {
            "Rojo" -> Color.RED
            "Verde" -> Color.GREEN
            "Azul" -> Color.BLUE
            "Amarillo" -> Color.YELLOW
            "Morado" -> Color.MAGENTA
            else -> Color.BLACK
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityfuncionales1)

        // Inicialización de los elementos de la UI
        etQuantum = findViewById(R.id.etQuantum)
        etNombreProceso = findViewById(R.id.etNombreProceso)
        etTiempoLlegada = findViewById(R.id.etTiempoLlegada)
        etRafaga = findViewById(R.id.etRafaga)
        spinnerColorProceso = findViewById(R.id.spinnerColorProceso)
        btnAnadir = findViewById(R.id.btnAnadir)

        // Cargar colores desde el string-array
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.color_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColorProceso.adapter = adapter

        btnAnadir.setOnClickListener {
            if (validarCampos()) {
                // Crear un nuevo proceso con los valores ingresados
                val nombre = etNombreProceso.text.toString()
                val quantum = etQuantum.text.toString().toInt()
                val tiempoLlegada = etTiempoLlegada.text.toString().toInt()
                val rafaga = etRafaga.text.toString().toInt()
                val color = obtenerColorSeleccionado(spinnerColorProceso.selectedItem.toString())

                val proceso = Proceso(nombre, quantum, tiempoLlegada, rafaga, color)
                listaProcesos.add(proceso)

                Toast.makeText(this, "Proceso agregado", Toast.LENGTH_LONG).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Rellene todos los campos del Proceso", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Clase de datos para representar un proceso
    data class Proceso(
        val nombre: String,
        val quantum: Int,
        val tiempoLlegada: Int,
        val rafaga: Int,
        val color: Int
    )
}
