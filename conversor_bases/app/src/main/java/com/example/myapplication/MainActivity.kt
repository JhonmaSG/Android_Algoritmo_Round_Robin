package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val num1: EditText = findViewById(R.id.edtnum1)
        val select: RadioGroup = findViewById(R.id.roper)
        val btncal: Button = findViewById(R.id.btncalcular)
        val result: TextView = findViewById(R.id.textView2)

        btncal.setOnClickListener {
            val n1 = num1.text.toString().toIntOrNull()
            val selectedOperationId = select.checkedRadioButtonId

            // Validar entradas
            if (n1 == null) {
                Toast.makeText(this, "Digite un número entero válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Realizar la operación seleccionada
            val res = when (selectedOperationId) {
                R.id.rbinario -> ConvBinario(n1)
                R.id.roctal -> convertToOctal(n1)
                R.id.rhexa -> convertToHexadecimal(n1)
                else -> null
            }

            // Mostrar el resultado
            result.text = if (res != null) "Resultado: $res" else "Resultado: ERROR"
        }
    }

    // Conversión a binario
    fun ConvBinario(number: Int): String {
        var num = number
        var binary = ""
        if (num == 0) return "0"
        while (num > 0) {
            binary = (num % 2).toString() + binary
            num /= 2
        }
        return binary
    }

    // Conversión a octal
     fun convertToOctal(number: Int): String {
        var num = number
        var octal = ""
        if (num == 0) return "0"
        while (num > 0) {
            octal = (num % 8).toString() + octal
            num /= 8
        }
        return octal
    }

    // Conversión a hexadecimal
    fun convertToHexadecimal(number: Int): String {
        var num = number
        var hex = ""
        val hexChars = "0123456789ABCDEF"
        if (num == 0) return "0"
        while (num > 0) {
            val remainder = num % 16
            hex = hexChars[remainder] + hex
            num /= 16
        }
        return hex
    }
}
