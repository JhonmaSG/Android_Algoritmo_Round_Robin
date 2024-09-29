package com.example.algoritmo_round_robin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.algoritmo_round_robin.ProcesoAdapter

class ProcesoAdapter(private val listaProcesos: List<activityFuncionales1.Proceso>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    // ViewHolder para el encabezado
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textColor: TextView = itemView.findViewById(R.id.tv_color)
        val textNombreProceso: TextView = itemView.findViewById(R.id.tv_proceso)
        val textTiempoLlegada: TextView = itemView.findViewById(R.id.tv_tiempo_llegada)
        val textRafaga: TextView = itemView.findViewById(R.id.tv_rafaga)
    }

    // ViewHolder para los ítems del proceso
    class ProcesoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewColor: View = itemView.findViewById(R.id.viewColor)
        val textNombreProceso: TextView = itemView.findViewById(R.id.textNombreProceso)
        val textTiempoLlegada: TextView = itemView.findViewById(R.id.textTiempoLlegada)
        val textRafaga: TextView = itemView.findViewById(R.id.textRafaga)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_item, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activityitem_proceso, parent, false)
            ProcesoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
        } else if (holder is ProcesoViewHolder) {
            val proceso = listaProcesos[position - 1] // Ajustar el índice debido al encabezado

            holder.textNombreProceso.text = proceso.nombre
            holder.textTiempoLlegada.text = proceso.tiempoLlegada.toString()
            holder.textRafaga.text = proceso.rafaga.toString()

            // Establecer el color del View
            try {
                Log.d("ProcesoAdapter", "Color a parsear: ${proceso.color}")
                holder.viewColor.setBackgroundColor(Color.parseColor(proceso.color))
            } catch (e: IllegalArgumentException) {
                Log.e("ProcesoAdapter", "Error al parsear color: ${proceso.color}", e)
                holder.viewColor.setBackgroundColor(Color.GRAY) // Color predeterminado
            }
        }
    }

    override fun getItemCount(): Int {
        return listaProcesos.size + 1 // +1 para el encabezado
    }
}


class activityFuncionales1 : AppCompatActivity() {

    // Variables globales
    private lateinit var etQuantum: EditText
    private lateinit var etNombreProceso: EditText
    private lateinit var etTiempoLlegada: EditText
    private lateinit var etRafaga: EditText
    private lateinit var spinnerColorProceso: Spinner
    private lateinit var btnAnadir: Button
    private lateinit var btnSimular: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProcesoAdapter

    private val listaProcesos = mutableListOf<Proceso>()

    // Variable para almacenar el valor del quantum
    private var quantum: Int? = null

    //Funcion para agregar los procesos a recycleView
    private fun agregarProceso(){

        // Si el quantum aún no se ha establecido, obténlo del EditText
        if (quantum == null) {
            val quantumInput = etQuantum.text.toString().toIntOrNull()
            if (quantumInput != null) {
                quantum = quantumInput
            } else {
                Toast.makeText(this, "Por favor, ingrese un valor de quantum válido", Toast.LENGTH_LONG).show()
                return
            }
        }

        val nombre = etNombreProceso.text.toString()
        val tiempoLlegada = etTiempoLlegada.text.toString().toIntOrNull()
        val rafaga = etRafaga.text.toString().toIntOrNull()
        val colorSeleccionado = spinnerColorProceso.selectedItem.toString()

        if (nombre.isNotBlank() && tiempoLlegada != null && rafaga != null && colorSeleccionado != "Seleccionar Color") {
            val nuevoProceso = Proceso(nombre, quantum!!, tiempoLlegada, rafaga, colorSeleccionado)
            listaProcesos.add(nuevoProceso)
            adapter.notifyDataSetChanged() // Refresca toda la lista
            Toast.makeText(this, "Proceso agregado", Toast.LENGTH_LONG).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
        }
    }

    // Función para validar si los campos están completos
    private fun validarCampos(): Boolean {
        return when {
            etNombreProceso.text.isEmpty() -> false
            etQuantum.text.isEmpty() -> false
            etTiempoLlegada.text.isEmpty() -> false
            etRafaga.text.isEmpty() -> false
            else -> true
        }
    }

    // Función para limpiar los campos después de agregar el proceso
    private fun limpiarCampos() {
        etNombreProceso.text.clear()
        etTiempoLlegada.text.clear()
        etRafaga.text.clear()
        spinnerColorProceso.setSelection(0) // Restablece a la primera opción
    }

    // Función para obtener el color basado en la selección del Spinner
    private fun obtenerColorSeleccionado(colorSeleccionado: String): Int {
        return when (colorSeleccionado) {
            "Rojo" -> Color.RED
            "Verde" -> Color.GREEN
            "Azul" -> Color.BLUE
            "Amarillo" -> Color.YELLOW
            "Morado" -> Color.MAGENTA
            else -> Color.BLACK
        }
    }

    private fun simular() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityfuncionales1)

        // Inicialización de los elementos de la UI
        etQuantum = findViewById(R.id.etQuantum)
        etNombreProceso = findViewById(R.id.etNombreProceso)
        etTiempoLlegada = findViewById(R.id.etTiempoLlegada)
        etRafaga = findViewById(R.id.etRafaga)
        spinnerColorProceso = findViewById(R.id.spinnerColorProceso)
        btnAnadir = findViewById(R.id.btnAnadir)
        btnSimular = findViewById(R.id.btnSimularAhora)
        recyclerView = findViewById(R.id.recyclerViewProcesos)

        // Cargar colores desde el string-array
        val adapterColor = ArrayAdapter.createFromResource(
            this,
            R.array.color_list,
            android.R.layout.simple_spinner_item
        )
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColorProceso.adapter = adapterColor

        // Inicializa el adaptador del RecyclerView
        adapter = ProcesoAdapter(listaProcesos)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configura el botón de añadir proceso
        btnAnadir.setOnClickListener {
            if (validarCampos()) {
                agregarProceso()
            } else {
                Toast.makeText(this, "Rellene todos los campos del Proceso", Toast.LENGTH_LONG).show()
            }
        }

        // Configura el botón de simular
        btnSimular.setOnClickListener {
            simular()
        }
    }

    // Clase de datos para representar un proceso
    data class Proceso(
        val nombre: String,
        val quantum: Int,
        val tiempoLlegada: Int,
        val rafaga: Int,
        val color: String
    )
}