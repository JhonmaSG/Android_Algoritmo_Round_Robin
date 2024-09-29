package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activityTeorica1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activityteorica1)
        /*Realizar un mini tutorial sobre el funcionamiento
        del algoritmo, se debe mostrar las formulas usadas
        para el proceso del calculo del algoritmo*/

        val btnSiguiente: Button = findViewById(R.id.btnContinuar)
        val btnRetroceder: Button = findViewById(R.id.btnAtras)

        btnSiguiente.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intentAvanzar = Intent(this, activityTeorica2::class.java)
            startActivity(intentAvanzar)
        }

        btnRetroceder.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intentAtras = Intent(this, MainActivity::class.java)
            startActivity(intentAtras)
        }
    }
}