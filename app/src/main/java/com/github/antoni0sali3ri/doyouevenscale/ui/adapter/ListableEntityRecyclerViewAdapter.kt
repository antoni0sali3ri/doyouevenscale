package com.github.antoni0sali3ri.doyouevenscale.ui.adapter

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
    override var items: List<T>,
    val onEdit: (Long) -> Unit,
    val onDelete: (Long) -> Unit
) : RecyclerView.Adapter<ListableEntityRecyclerViewAdapter.ViewHolder>(), HasItems<T> {

    protected open val itemLayout = R.layout.list_item_data_manager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.idView.text = item.id.toString()
        holder.contentView.text = item.name
        holder.btnEdit.setOnClickListener { onEdit(item.id) }
        holder.btnDelete.setOnClickListener { onDelete(item.id) }
    }

    override fun getItemCount(): Int = items.size

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.itemId)
        val contentView: TextView = view.findViewById(R.id.itemTitle)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditItem)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteItem)
    }
}

interface HasItems<T> {
    var items: List<T>
}