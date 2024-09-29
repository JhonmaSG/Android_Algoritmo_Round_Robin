package com.example.algoritmo_round_robin

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class activityCreditos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Opción para UI inmersiva
        setContentView(R.layout.activity_creditos)

        val txtTitle: TextView = findViewById(R.id.txtTitle)
        val imageView: ImageView = findViewById(R.id.imageView)

        val Integrante1: TextView = findViewById(R.id.Integrante1)
        val Integrante2: TextView = findViewById(R.id.Integrante2)
        val Integrante3: TextView = findViewById(R.id.Integrante3)
        val Integrante4: TextView = findViewById(R.id.Integrante4)

        val btnAtras: Button = findViewById(R.id.btnAtras)

        // Animación Fade In para título y logo
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        // Aplicar Fade In al título y al logo
        txtTitle.startAnimation(fadeIn)
        imageView.startAnimation(fadeIn)

        // Animación Slide Up para los nombres
        val slideUp = TranslateAnimation(0f, 0f, 300f, 0f).apply {
            duration = 1000
            fillAfter = true
        }

        // Aplicar Slide Up a los nombres
        Integrante1.startAnimation(slideUp)
        Integrante2.startAnimation(slideUp)
        Integrante3.startAnimation(slideUp)
        Integrante4.startAnimation(slideUp)

        // Animación de pulsing para el botón "Atras"
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f, 1f)

        val pulsingAnimator = ObjectAnimator.ofPropertyValuesHolder(btnAtras, scaleX, scaleY).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        // Iniciar la animación pulsing del botón
        pulsingAnimator.start()

        // Animación de rotación para el logo
        val rotate = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
        }

        // Iniciar la animación de rotación
        rotate.start()

        btnAtras.setOnClickListener {
            finish()  // Termina la actividad actual
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}
