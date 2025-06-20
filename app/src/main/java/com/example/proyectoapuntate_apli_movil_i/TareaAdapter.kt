package com.example.proyectoapuntate_apli_movil_i

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.proyectoapuntate_apli_movil_i.Entidades.Tarea
import java.text.SimpleDateFormat
import java.util.*

class TareaAdapter(
    private val context: Context,
    private val tareas: List<Tarea>,
    private val onEditar: (Tarea) -> Unit,
    private val onEliminar: (Tarea) -> Unit
) : ArrayAdapter<Tarea>(context, 0, tareas) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tarea = tareas[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false)

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo)
        val tvDescripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val tvEstado = view.findViewById<TextView>(R.id.tvEstado)
        val tvFecha = view.findViewById<TextView>(R.id.tvFecha)
        val btnEditar = view.findViewById<Button>(R.id.btnEditar)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminar)

        tvTitulo.text = tarea.Titulo
        tvDescripcion.text = tarea.Descripcion
        tvEstado.text = tarea.Estado
        tvFecha.text = dateFormat.format(tarea.Fecha)

        btnEditar.setOnClickListener { onEditar(tarea) }
        btnEliminar.setOnClickListener { onEliminar(tarea) }

        return view
    }
}