package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*Bienvenida al tutorial del Algoritmo de Planificación*/
        val btncomenzar : Button = findViewById(R.id.btnComenzar);

        btncomenzar.setOnClickListener{
            val intentComenzar = Intent(this,activityTeorica1::class.java).apply{}
            startActivity(intentComenzar)
        }
    }
}