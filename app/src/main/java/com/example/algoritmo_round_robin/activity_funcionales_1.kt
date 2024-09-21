package com.example.algoritmo_round_robin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_funcionales_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_funcionales_1)
        // Acá se debe realizar un ejemplo funcional del algoritmo Round Robin donde se evidencie
        // todo el proceso desde la carga de los procesos:
        // - asignación de tiempos de ráfaga,
        // - linea de tiempo
        // - calculo del tiempo de espera
        // - finalización de cada proceso,
        // - tiempo promedio de espera
        // - tiempo promedio de finalización

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}