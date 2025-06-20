package com.example.proyectoapuntate_apli_movil_i

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper
import com.example.proyectoapuntate_apli_movil_i.Entidades.Tarea
import java.util.*

class TareasActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var tareasListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var tareas: List<Tarea>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        dbHelper = DBHelper(this)
        tareasListView = findViewById(R.id.listViewTareas)

        // Mostrar todas las tareas
        cargarTareas()

        // Botón para agregar tarea (deberías tener un botón en tu layout)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarTarea)
        btnAgregar.setOnClickListener {
            // Aquí puedes abrir un diálogo o una nueva pantalla para agregar una tarea
            agregarTareaDemo()
            cargarTareas()
        }

        // Puedes agregar listeners para editar/eliminar tareas aquí
        tareasListView.setOnItemClickListener { _, _, position, _ ->
            val tarea = tareas[position]
            // Aquí puedes abrir una pantalla para editar/eliminar la tarea seleccionada
            Toast.makeText(this, "Seleccionaste: ${tarea.Titulo}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarTareas() {
        tareas = dbHelper.getAllTareas()
        val titulos = tareas.map { "${it.Titulo} (${it.Estado})" }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titulos)
        tareasListView.adapter = adapter
    }

    private fun agregarTareaDemo() {
        // Esto es solo un ejemplo, deberías mostrar un form para ingresar datos reales
        val nuevaTarea = Tarea(
            ApunteId = 1, // Cambia esto por el ApunteId real
            Titulo = "Nueva tarea",
            Descripcion = "Descripción de la tarea",
            Estado = "Pendiente",
            Fecha = Date()
        )
        dbHelper.insertTarea(nuevaTarea)
    }
}