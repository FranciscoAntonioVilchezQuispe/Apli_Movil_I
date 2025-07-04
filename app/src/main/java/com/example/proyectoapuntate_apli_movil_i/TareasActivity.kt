package com.example.proyectoapuntate_apli_movil_i
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper
import com.example.proyectoapuntate_apli_movil_i.Entidades.Tarea
import java.util.*

class TareasActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var tareasListView: ListView
    private lateinit var adapter: TareaAdapter
    private var tareas: MutableList<Tarea> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        dbHelper = DBHelper(this)
        tareasListView = findViewById(R.id.listViewTareas)

        cargarTareas()

        val btnAgregar = findViewById<Button>(R.id.btnAgregarTarea)
        btnAgregar.setOnClickListener {
            mostrarDialogoAgregar(null)
        }
    }

    private fun cargarTareas() {
        tareas = dbHelper.getAllTareas().toMutableList()
        adapter = TareaAdapter(
            this,
            tareas,
            onEditar = { tarea -> mostrarDialogoAgregar(tarea) },
            onEliminar = { tarea -> mostrarDialogoEliminar(tarea) }
        )
        tareasListView.adapter = adapter
    }

    // Mostrar Dialog para agregar o editar tarea
    private fun mostrarDialogoAgregar(tarea: Tarea?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tarea, null)
        val etTitulo = dialogView.findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)

        if (tarea != null) {
            etTitulo.setText(tarea.Titulo)
            etDescripcion.setText(tarea.Descripcion)
        }

        AlertDialog.Builder(this)
            .setTitle(if (tarea == null) "Agregar tarea" else "Editar tarea")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoTitulo = etTitulo.text.toString()
                val nuevaDescripcion = etDescripcion.text.toString()
                if (nuevoTitulo.isNotBlank()) {
                    if (tarea == null) {
                        val nuevaTarea = Tarea(
                            ApunteId = 1, // Cambia esto si necesitas otro ApunteId
                            Titulo = nuevoTitulo,
                            Descripcion = nuevaDescripcion,
                            Estado = "Pendiente",
                            Fecha = Date()
                        )
                        dbHelper.insertTarea(nuevaTarea)
                    } else {
                        tarea.Titulo = nuevoTitulo
                        tarea.Descripcion = nuevaDescripcion
                        dbHelper.updateTarea(tarea)
                    }
                    cargarTareas()
                } else {
                    Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(tarea: Tarea) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar tarea")
            .setMessage("¿Seguro deseas eliminar la tarea '${tarea.Titulo}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                dbHelper.deleteTarea(tarea.TareaId)
                cargarTareas()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}