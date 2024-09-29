package com.example.algoritmo_round_robin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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

    private fun mostrarResultados(tiempoEsperaPromedio: Double, tiempoFinalizacionPromedio: Double) {
        val resultados = StringBuilder()
        resultados.append("Resultados de la Simulación Round Robin:\n")
        resultados.append("Valor del Quantum: ${quantum}\n\n")
        listaProcesos.forEach { proceso ->
            resultados.append("Color: ${proceso.color}\n")
            resultados.append("Proceso: ${proceso.nombre}\n")
            resultados.append("Tiempo de llegada: ${proceso.tiempoLlegada}\n")
            resultados.append("Ráfaga: ${proceso.rafaga}\n")
            resultados.append("Tiempo de finalización: ${proceso.tiempoFinalizacion}\n")
            resultados.append("Tiempo de espera: ${proceso.tiempoEspera}\n")
            resultados.append("\n")
        }

        resultados.append("Tiempo promedio de espera: $tiempoEsperaPromedio\n")
        resultados.append("Tiempo promedio de finalización: $tiempoFinalizacionPromedio\n")

        // Crear un Intent para abrir la nueva actividad y pasar los resultados
        val intent = Intent(this, activityFuncionales2::class.java)
        intent.putExtra("resultados", resultados.toString())
        startActivity(intent)

    }

    private fun simular() {
        if (listaProcesos.isEmpty()) {
            Toast.makeText(this, "No hay procesos para simular", Toast.LENGTH_SHORT).show()
            return
        }

        val colaProcesos = listaProcesos.toMutableList() // Crear una lista mutable de procesos
        colaProcesos.sortBy { it.tiempoLlegada } // Ordenar por tiempo de llegada
        var tiempoTotal = 0 // Tiempo total transcurrido
        val quantum = quantum ?: return // Asegurarnos de que el quantum esté establecido

        // Imprimir la lista de procesos ordenada
        println("Lista de procesos ordenada por tiempo de llegada:")
        colaProcesos.forEach { println(it) }

        // Mientras haya procesos con tiempo de ráfaga restante
        while (colaProcesos.isNotEmpty()) {
            val iterator = colaProcesos.iterator()

            while (iterator.hasNext()) {
                val proceso = iterator.next()

                if (proceso.tiempoRestante > 0) {
                    // Si el proceso puede ejecutarse completamente dentro del quantum
                    if (proceso.tiempoRestante <= quantum) {
                        tiempoTotal += proceso.tiempoRestante // Aumentar el tiempo total
                        proceso.tiempoRestante = 0 // El proceso ha terminado
                        proceso.tiempoFinalizacion = tiempoTotal // Registrar el tiempo de finalización
                    } else {
                        // Si no puede completarse, ejecuta por el quantum y resta tiempo restante
                        proceso.tiempoRestante -= quantum
                        tiempoTotal += quantum
                    }
                }

                // Si el proceso ha terminado, calcular su tiempo de espera
                if (proceso.tiempoRestante == 0) {
                    proceso.tiempoEspera = proceso.tiempoFinalizacion - proceso.tiempoLlegada - proceso.rafaga
                    iterator.remove() // Remover el proceso de la cola
                }
            }
        }

        // Cálculo de los tiempos promedios
        val tiempoEsperaPromedio = listaProcesos.sumOf { it.tiempoEspera } / listaProcesos.size.toDouble()
        val tiempoFinalizacionPromedio = listaProcesos.sumOf { it.tiempoFinalizacion } / listaProcesos.size.toDouble()

        // Mostrar los resultados
        mostrarResultados(tiempoEsperaPromedio, tiempoFinalizacionPromedio)
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
        val nombre: String, //Nombre del proceso
        val quantum: Int,   //quantum
        val tiempoLlegada: Int, //tiempo de llegada
        val rafaga: Int,    //rafaga
        val color: String,  //color del proceso
        var tiempoRestante: Int = rafaga, //tiempo que le queda por ejecutar
        var tiempoEspera: Int = 0, // tiempo total de espera
        var tiempoFinalizacion: Int = 0 //tiempo en que termina
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(nombre)
            parcel.writeInt(quantum)
            parcel.writeInt(tiempoLlegada)
            parcel.writeInt(rafaga)
            parcel.writeString(color)
            parcel.writeInt(tiempoRestante)
            parcel.writeInt(tiempoEspera)
            parcel.writeInt(tiempoFinalizacion)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Proceso> {
            override fun createFromParcel(parcel: Parcel): Proceso {
                return Proceso(parcel)
            }

            override fun newArray(size: Int): Array<Proceso?> {
                return arrayOfNulls(size)
            }
        }
    }
}