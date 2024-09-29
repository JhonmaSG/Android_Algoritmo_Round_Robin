package com.example.algoritmo_round_robin

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*Bienvenida al tutorial del Algoritmo de Planificación*/
        val btncomenzar : Button = findViewById(R.id.btnComenzar)
        val btnCreditos : Button = findViewById(R.id.btnCreditos)

        val imagenPrincipal : ImageView = findViewById(R.id.imgLogo)
        val txtTitle : TextView = findViewById(R.id.txtTitle)

        btncomenzar.setOnClickListener{
            val intentComenzar = Intent(this,activityTeorica1::class.java).apply{}
            startActivity(intentComenzar)
        }

        btnCreditos.setOnClickListener{
            val intentCreditos = Intent(this,activityCreditos::class.java).apply{}
            startActivity(intentCreditos)
        }

        //ANIMACIONES

        // Animación Fade In para título y logo
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        // Aplicar Fade In al título y al logo
        txtTitle.startAnimation(fadeIn)
        imagenPrincipal.startAnimation(fadeIn)

        // Animación de rotación para el logo
        val rotate = ObjectAnimator.ofFloat(imagenPrincipal, "rotation", 0f, 360f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
        }

        // Iniciar la animación de rotación
        rotate.start()
    }
}