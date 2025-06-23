package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoapuntate_apli_movil_i.Entidades.Notas
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class NotasActivity : AppCompatActivity() {
    private lateinit var recyclerViewNotas: RecyclerView
    private lateinit var notasAdapter: NotasAdapter
    private lateinit var editTextBuscarNotas: EditText
    private lateinit var imageViewNotification: ImageView
    private lateinit var buttonAddNota: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var notasDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        notasDBHelper = DBHelper(this)

        recyclerViewNotas = findViewById(R.id.recyclerViewNotas)
        editTextBuscarNotas = findViewById(R.id.editTextBuscarNotas)
        imageViewNotification = findViewById(R.id.imageViewNotification)
        buttonAddNota = findViewById(R.id.buttonAddNota)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Configurar RecyclerView con callbacks para editar y eliminar
        notasAdapter = NotasAdapter(
            notas = notasDBHelper.obtenerTodasLasNotas(),
            onEditClick = { nota -> showAddEditNoteDialog(nota) }, // Pasar la nota para editar
            onDeleteClick = { nota -> confirmDeleteNote(nota) }    // Pasar la nota para eliminar
        )
        recyclerViewNotas.layoutManager = LinearLayoutManager(this)
        recyclerViewNotas.adapter = notasAdapter

        // Lógica para el botón de añadir nota
        buttonAddNota.setOnClickListener {
            showAddEditNoteDialog(null) // Pasar null para indicar que es una nueva nota
        }

        // Lógica para la barra de navegación inferior
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Ya estamos en Home/Notas, no hacer nada o recargar
                    true
                }
                R.id.navigation_clientes -> {
                    // Navegar a MainActivity (listado de clientes)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra esta actividad para no acumularla en el backstack
                    true
                }
                R.id.navigation_profile -> {
                    Toast.makeText(this, "Ir a Perfil (Funcionalidad pendiente)", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        // Asegúrate de que el ítem de "Home" esté seleccionado al iniciar esta Activity
        bottomNavigationView.selectedItemId = R.id.navigation_home

        // Lógica para el EditText de búsqueda (simple filtrado por título)
        editTextBuscarNotas.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotas(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Inicializa la lista de notas al abrir la actividad
        loadNotas()
    }

    private fun loadNotas() {
        val notas = notasDBHelper.obtenerTodasLasNotas()
        notasAdapter.updateList(notas)
        showSnackbarCount(notas.size)
    }

    private fun filterNotas(query: String) {
        val allNotas = notasDBHelper.obtenerTodasLasNotas()
        val filteredList = if (query.isEmpty()) {
            allNotas
        } else {
            allNotas.filter {
                it.titulo.contains(query, ignoreCase = true)
            }
        }
        notasAdapter.updateList(filteredList)
        showSnackbarCount(filteredList.size)
    }

    private fun showAddEditNoteDialog(notaToEdit: Notas?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_nota, null)
        val editTextTitulo = dialogView.findViewById<EditText>(R.id.editTextDialogTituloNota)
        val seekBarProgreso = dialogView.findViewById<SeekBar>(R.id.seekBarDialogProgreso)
        val textViewProgresoValor = dialogView.findViewById<TextView>(R.id.textViewDialogProgresoValor)

        // Pre-rellenar campos si estamos editando
        if (notaToEdit != null) {
            editTextTitulo.setText(notaToEdit.titulo)
            seekBarProgreso.progress = notaToEdit.progreso
            textViewProgresoValor.text = "${notaToEdit.progreso}%"
        } else {
            // Para nueva nota, asegurar que el progreso inicial sea 0 y se muestre
            seekBarProgreso.progress = 0
            textViewProgresoValor.text = "0%"
        }

        seekBarProgreso.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewProgresoValor.text = "$progress%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        AlertDialog.Builder(this)
            .setTitle(if (notaToEdit == null) "Agregar Nueva Nota" else "Editar Nota")
            .setView(dialogView)
            .setPositiveButton(if (notaToEdit == null) "Agregar" else "Guardar") { dialog, _ ->
                val titulo = editTextTitulo.text.toString().trim()
                val progreso = seekBarProgreso.progress

                if (titulo.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "El título de la nota no puede estar vacío.", Snackbar.LENGTH_SHORT).show()
                } else {
                    if (notaToEdit == null) {
                        // Es una nueva nota
                        val nuevaNota = Notas(0, titulo, progreso) // ID 0, será auto-generado
                        val id = notasDBHelper.insertarNota(nuevaNota)
                        if (id != -1L) {
                            Snackbar.make(findViewById(android.R.id.content), "Nota '$titulo' agregada.", Snackbar.LENGTH_SHORT).show()
                            loadNotas() // Recargar la lista
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error al agregar nota.", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        // Es una edición de nota existente
                        val notaActualizada = Notas(notaToEdit.id, titulo, progreso)
                        val rowsAffected = notasDBHelper.actualizarNota(notaActualizada)
                        if (rowsAffected > 0) {
                            Snackbar.make(findViewById(android.R.id.content), "Nota '$titulo' actualizada.", Snackbar.LENGTH_SHORT).show()
                            loadNotas() // Recargar la lista
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error al actualizar nota.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun confirmDeleteNote(nota: Notas) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Nota")
            .setMessage("¿Estás seguro de que quieres eliminar la nota '${nota.titulo}'?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                val rowsAffected = notasDBHelper.eliminarNota(nota.id)
                if (rowsAffected > 0) {
                    Snackbar.make(findViewById(android.R.id.content), "Nota '${nota.titulo}' eliminada.", Snackbar.LENGTH_SHORT).show()
                    loadNotas() // Recargar la lista
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Error al eliminar nota.", Snackbar.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showSnackbarCount(count: Int) {
        val message = "Se encontraron $count apuntes"
        Snackbar.make(recyclerViewNotas, message, Snackbar.LENGTH_INDEFINITE).show()
    }
}