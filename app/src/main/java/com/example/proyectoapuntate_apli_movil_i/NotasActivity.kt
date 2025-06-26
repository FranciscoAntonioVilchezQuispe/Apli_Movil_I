package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoapuntate_apli_movil_i.Entidades.Notas
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class NotasActivity : AppCompatActivity() {
    private lateinit var recyclerViewNotas: RecyclerView
    private lateinit var notasAdapter: NotasAdapter
    private lateinit var editTextBuscarNotas: EditText
    private lateinit var imageViewNotification: ImageView
    private lateinit var buttonAddNota: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    //private var userId: String? = null
    //private var iUserId: Int? = null

    private lateinit var notasDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)
       // userId = intent.getStringExtra("USER_ID")
      //  iUserId = userId?.toIntOrNull()

        notasDBHelper = DBHelper(this)

        recyclerViewNotas = findViewById(R.id.recyclerViewNotas)
        editTextBuscarNotas = findViewById(R.id.editTextBuscarNotas)
        imageViewNotification = findViewById(R.id.imageViewNotification)
        buttonAddNota = findViewById(R.id.buttonAddNota)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        notasAdapter = NotasAdapter(
            notas = notasDBHelper.obtenerTodasLasNotas(),
            onEditClick = { nota -> showAddEditNoteDialog(nota) },
            onDeleteClick = { nota -> confirmDeleteNote(nota) }
        )
        recyclerViewNotas.layoutManager = LinearLayoutManager(this)
        recyclerViewNotas.adapter = notasAdapter

        buttonAddNota.setOnClickListener {
            showAddEditNoteDialog(null)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.navigation_notas -> {
                    val intent = Intent(this, NotasActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }
        }

        editTextBuscarNotas.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotas(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

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

        if (notaToEdit != null) {
            editTextTitulo.setText(notaToEdit.titulo)
            seekBarProgreso.progress = notaToEdit.progreso
            textViewProgresoValor.text = "${notaToEdit.progreso}%"
        } else {
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
                        val nuevaNota = Notas(0, titulo, progreso)
                        val id = notasDBHelper.insertarNota(nuevaNota)
                        if (id != -1L) {
                            Snackbar.make(findViewById(android.R.id.content), "Nota '$titulo' agregada.", Snackbar.LENGTH_SHORT).show()
                            loadNotas()
                        } else {
                            Snackbar.make(findViewById(android.R.id.content),
                                "Error al agregar nota.$nuevaNota", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        val notaActualizada = Notas(notaToEdit.id, titulo, progreso)
                        val rowsAffected = notasDBHelper.actualizarNota(notaActualizada)
                        if (rowsAffected > 0) {
                            Snackbar.make(findViewById(android.R.id.content), "Nota '$titulo' actualizada.", Snackbar.LENGTH_SHORT).show()
                            loadNotas()
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
                    loadNotas()
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
      //  Snackbar.make(recyclerViewNotas, message, Snackbar.LENGTH_INDEFINITE).show()
    }
}