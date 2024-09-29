package com.example.algoritmo_round_robin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activityFuncionales2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_funcionales2)

        val textViewResultados = findViewById<TextView>(R.id.textViewDatos)

        val btnPasoaPaso: Button = findViewById(R.id.btnPasoaPaso)
        val btnRetroceder: Button = findViewById(R.id.btnAtras)

        // Recibir los datos desde el Intent
        val resultados = intent.getStringExtra("resultados")

        // Mostrar los resultados en el TextView
        textViewResultados.text = resultados

        /*
        btnPasoaPaso.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intentAvanzar = Intent(this, activityFuncionales3::class.java)
            startActivity(intentAvanzar)
        }
        */


        btnRetroceder.setOnClickListener {
            // Inicia una nueva actividad o haz otra acción
            val intentAtras = Intent(this, activityFuncionales1::class.java)
            startActivity(intentAtras)
        }



    }
}