package com.example.operadores

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val num1: EditText = findViewById(R.id.edtnum1)
        val num2: EditText = findViewById(R.id.edtnum2)
        val select: RadioGroup = findViewById(R.id.roper)
        val btncal: Button = findViewById(R.id.btncalcular)
        val result: TextView = findViewById(R.id.textView2)

        btncal.setOnClickListener {
            val n1 = num1.text.toString().toDoubleOrNull()
            val n2 = num2.text.toString().toDoubleOrNull()
            val selectedOperationId = select.checkedRadioButtonId

            // Validar entradas
            if (n1 == null || n2 == null) {
                Toast.makeText(this, "Digite los números válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Realizar la operación seleccionada
            val res = when (selectedOperationId) {
                R.id.rsuma -> n1 + n2
                R.id.rresta -> n1 - n2
                R.id.rmul -> n1 * n2
                R.id.rdiv -> {
                    if (n2 != 0.0) n1 / n2
                    else {
                        Toast.makeText(this, "ERROR!! No se puede dividir por 0", Toast.LENGTH_SHORT).show()
                        null
                    }
                }
                else -> null
            }

            // Mostrar el resultado
            if (res != null) {
                result.text = if(res !=null) "Resultado: $res" else "Resultado: ERROR:p"
            }
        }
    }
}
