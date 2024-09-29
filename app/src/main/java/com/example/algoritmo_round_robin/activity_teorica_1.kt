package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_teorica_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teorica_1)
        /*Realizar un mini tutorial sobre el funcionamiento
        del algoritmo, se debe mostrar las formulas usadas
        para el proceso del calculo del algoritmo*/

        val btnSiguiente: Button = findViewById(R.id.btnContinuar)

        btnSiguiente.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intent = Intent(this, activity_teorica_2::class.java)
            startActivity(intent)
        }
    }
}