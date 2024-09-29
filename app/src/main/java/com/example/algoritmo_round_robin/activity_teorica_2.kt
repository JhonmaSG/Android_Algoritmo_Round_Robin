package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_teorica_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teorica_2)
        val btnSiguiente: Button = findViewById(R.id.btnComenzar)

        // Manejar el clic del botón para avanzar
        btnSiguiente.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intent = Intent(this, activity_funcionales_1::class.java)
            startActivity(intent)
        }
    }
}