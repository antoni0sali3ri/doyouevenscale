package com.github.antoni0sali3ri.doyouevenscale.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import java.util.*

class InstrumentPresetListAdapter(
    override var items: List<InstrumentPreset>,
    val dragListener: OnStartDragListener,
    val onItemsUpdated: (List<InstrumentPreset>) -> Unit,
    val onEditItem: (Long) -> Unit,
    val onDeleteItem: (Long) -> Unit
) : RecyclerView.Adapter<InstrumentPresetListAdapter.ViewHolder>(), HasItems<InstrumentPreset> {

    val itemsMutable: MutableList<InstrumentPreset> = mutableListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.itemId)
        val txtName: TextView = view.findViewById(R.id.itemTitle)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditItem)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteItem)
        val dragHandle: ImageView = view.findViewById(R.id.dragHandle)
        val btnVisible: ImageButton = view.findViewById(R.id.btnShowAsTab)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_data_manager_draggable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val id = item.id
        holder.txtId.text = id.toString()
        holder.txtName.text = items[position].name

        if (item.showAsTab < 0) {
            holder.btnVisible.setImageResource(R.drawable.ic_visibility_off)
        } else {
            holder.btnVisible.setImageResource(R.drawable.ic_visibility)
        }
        holder.btnVisible.setOnClickListener {
            toggleItemVisibility(id, it as ImageButton)
        }

        holder.btnEdit.setOnClickListener { onEditItem(id) }

        holder.btnDelete.setOnClickListener { onDeleteItem(id) }
        if (item.showAsTab < 0) {
            holder.dragHandle.visibility = View.INVISIBLE
        } else {
            holder.dragHandle.visibility = View.VISIBLE
            holder.dragHandle.setOnTouchListener { _, ev ->
                if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(holder)
                }
                return@setOnTouchListener false
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val fromItem = itemsMutable[fromPosition]
        val targetItem = itemsMutable[toPosition]
        if (targetItem.showAsTab >= 0) {
            Log.d(
                "InstrumentPresetAdapter",
                "onItemMoved(fromItem[$fromPosition] = $fromItem, toItem[$toPosition] = $targetItem"
            )
            fromItem.showAsTab = toPosition
            targetItem.showAsTab = fromPosition
            Log.d(
                "InstrumentPresetAdapter",
                "onItemMoved(fromItem[$fromPosition] = $fromItem, toItem[$toPosition] = $targetItem"
            )
            Collections.swap(itemsMutable, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    private fun toggleItemVisibility(itemId: Long, btn: ImageButton) {
        val itemIndex = items.indexOfFirst { it.id == itemId }
        val item = items[itemIndex]
        if (item.showAsTab >= 0) {
            item.showAsTab = -1
            var i = itemIndex
            do {
                i += 1
                if (i >= items.size)
                    break
                val curItem = items[i]
                if (curItem.showAsTab < 0)
                    break
                else
                    curItem.showAsTab -= 1
            } while (true)
            btn.setImageResource(R.drawable.ic_visibility)
        } else {
            val numTabs = 1 + items.map { it.showAsTab }.maxOrNull()!!
            item.showAsTab = numTabs
            btn.setImageResource(R.drawable.ic_visibility_off)
        }
        updateItems()
    }

    fun updateItems() {
        val itemsSorted = itemsMutable.sortedWith(InstrumentPresetComparator)
        Log.d("InstrumentPresetAdapter", "updateItems() itemsSorted = $itemsSorted")
        onItemsUpdated(itemsSorted)
    }
}

interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}

class PresetListItemTouchHelperCallback(val adapter: InstrumentPresetListAdapter) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (viewHolder == null) {
            adapter.updateItems()
        }
    }

}

val InstrumentPresetComparator = object : Comparator<InstrumentPreset> {
    override fun compare(o1: InstrumentPreset?, o2: InstrumentPreset?): Int {
        if (o1 == null) {
            return if (o2 == null) 0 else -1
        } else if (o2 == null) {
            return 1
        } else {
            if (o1.showAsTab < 0) {
                if (o2.showAsTab < 0)
                    return o1.name.compareTo(o2.name)
                else
                    return 1
            } else {
                if (o2.showAsTab < 0)
                    return -1
                else
                    return o1.showAsTab.compareTo(o2.showAsTab)
            }
        }
    }
}
