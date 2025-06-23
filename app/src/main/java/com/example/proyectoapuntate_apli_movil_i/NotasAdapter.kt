package com.example.proyectoapuntate_apli_movil_i

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoapuntate_apli_movil_i.Entidades.Notas

class NotasAdapter(
    private var notas: List<Notas>,
    private val onEditClick: (Notas) -> Unit,
    private val onDeleteClick: (Notas) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitulo: TextView = itemView.findViewById(R.id.textViewTituloNota)
        val progressBarNota: ProgressBar = itemView.findViewById(R.id.progressBarNota)
        val textViewProgreso: TextView = itemView.findViewById(R.id.textViewProgreso)
        val imageViewMenuNota: ImageView = itemView.findViewById(R.id.imageViewMenuNota) // Nuevo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notas, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.textViewTitulo.text = nota.titulo
        holder.progressBarNota.progress = nota.progreso
        holder.textViewProgreso.text = "${nota.progreso}%"

        holder.imageViewMenuNota.setOnClickListener {
            val context = holder.itemView.context
            val popupMenu = android.widget.PopupMenu(context, holder.imageViewMenuNota)
            popupMenu.menuInflater.inflate(R.menu.nota_item_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit_note -> {
                        onEditClick(nota)
                        true
                    }
                    R.id.action_delete_note -> {
                        onDeleteClick(nota)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    fun updateList(newList: List<Notas>) {
        notas = newList
        notifyDataSetChanged()
    }
}