package com.github.antoni0sali3ri.doyouevenscale.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity

/**
 */
open class ListableEntityRecyclerViewAdapter<T : ListableEntity>(
    var values: List<T>,
    val onEdit: (Long) -> Unit,
    val onDelete: (Long) -> Unit
) : RecyclerView.Adapter<ListableEntityRecyclerViewAdapter<T>.ViewHolder>() {

    protected open val itemLayout = R.layout.list_item_data_manager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id.toString()
        holder.contentView.text = item.name
        holder.btnEdit.setOnClickListener { onEdit(item.id) }
        holder.btnDelete.setOnClickListener { onDelete(item.id) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.itemId)
        val contentView: TextView = view.findViewById(R.id.itemTitle)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditItem)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteItem)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}