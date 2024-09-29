package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activityTeorica2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activityteorica2)
        val btnSiguiente: Button = findViewById(R.id.btnComenzar)

        // Manejar el clic del botón para avanzar
        btnSiguiente.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intent = Intent(this, activityFuncionales1::class.java)
            startActivity(intent)
        }
    }
}