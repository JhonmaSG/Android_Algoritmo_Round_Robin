package com.example.parcial1

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class RoundRobinActivity : AppCompatActivity() {

    public lateinit var llProcesos: LinearLayout
    public var procesoCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_robin)

        llProcesos = findViewById(R.id.llProcesos)
        val btnAgregarProceso = findViewById<Button>(R.id.btnAgregarProceso)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val etQuantum = findViewById<EditText>(R.id.etQuantum)

        // Agregar dinámicamente un nuevo proceso (campo de ráfaga y llegada)
        btnAgregarProceso.setOnClickListener {
            procesoCounter++
            agregarCampoProceso(procesoCounter)
        }
        btnCalcular.setOnClickListener {
            try {
                // Validar que el campo Quantum no esté vacío y que sea un número
                val quantumText = etQuantum.text.toString()
                val quantum = quantumText.toIntOrNull()

                if (quantum == null || quantum <= 0) {
                    etQuantum.error = "Ingrese un quantum válido mayor que 0"
                    return@setOnClickListener
                }

                val rafagas = mutableListOf<Int>()
                val ordenesLlegada = mutableListOf<Int>()

                // Obtener los valores ingresados de ráfagas y órdenes de llegada
                for (i in 0 until procesoCounter) {
                    val rafagaText = llProcesos.findViewWithTag<EditText>("rafaga$i")
                    val llegadaText = llProcesos.findViewWithTag<EditText>("llegada$i")

                    val rafaga = rafagaText?.text?.toString()?.toIntOrNull()
                    val llegada = llegadaText?.text?.toString()?.toIntOrNull()

                    // Validar que las ráfagas y órdenes de llegada no sean nulas o negativas
                    if (rafaga == null || rafaga <= 0) {
                        rafagaText.error = "Ingrese una ráfaga válida mayor que 0"
                        return@setOnClickListener
                    }

                    if (llegada == null || llegada < 0) {
                        llegadaText.error = "Ingrese una llegada válida (0 o mayor)"
                        return@setOnClickListener
                    }

                    rafagas.add(rafaga)
                    ordenesLlegada.add(llegada)
                }

                // Log para depurar
                Log.d(
                    "RoundRobinActivity",
                    "Quantum: $quantum, Rafagas: $rafagas, OrdenesLlegada: $ordenesLlegada"
                )

                // Iniciar la actividad de resultados
                val intent = Intent(this, ResultadosActivity::class.java).apply {
                    putExtra("quantum", quantum)
                    putIntegerArrayListExtra("rafagas", ArrayList(rafagas))
                    putIntegerArrayListExtra("ordenesLlegada", ArrayList(ordenesLlegada))
                }

                startActivity(intent)
            } catch (e: Exception) {
                // Capturar cualquier excepción inesperada y loguearla
                Log.e("RoundRobinActivity", "Error: ${e.message}", e)
            }
        }

    }// Método para agregar dinámicamente campos para un nuevo proceso
    public fun agregarCampoProceso(numProceso: Int) {
        val rafagaText = EditText(this)
        rafagaText.hint = "Ráfaga P$numProceso"
        rafagaText.inputType = InputType.TYPE_CLASS_NUMBER
        rafagaText.tag = "rafaga$numProceso"
        llProcesos.addView(rafagaText)

        val llegadaText = EditText(this)
        llegadaText.hint = "Llegada P$numProceso"
        llegadaText.inputType = InputType.TYPE_CLASS_NUMBER
        llegadaText.tag = "llegada$numProceso"
        llProcesos.addView(llegadaText)
    }
}

