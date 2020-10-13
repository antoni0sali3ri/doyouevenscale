package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.core.model.PresetViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.InstrumentPresetComparator
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.InstrumentPresetRecyclerViewAdapter
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.OnStartDragListener
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.PresetListItemTouchHelperCallback

class InstrumentPresetListFragment :
    EntityListFragment<InstrumentPreset>(InstrumentPreset::class.java), OnStartDragListener {

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: InstrumentPresetRecyclerViewAdapter

    override val viewModel: PresetViewModel by activityViewModels()

    override fun setUpRecyclerView(recyclerView: RecyclerView, items: List<InstrumentPreset>) {
        val adapter = InstrumentPresetRecyclerViewAdapter(
            items,
            this,
            onUpdateItems,
            onEditItem,
            onDeleteItem
        )
        this.adapter = adapter
        recyclerView.adapter = adapter
        val callback = PresetListItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun setItems(items: List<InstrumentPreset>) {
        adapter.items = items.sortedWith(InstrumentPresetComparator)
        adapter.notifyDataSetChanged()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private val onUpdateItems: (List<InstrumentPreset>) -> Unit = { items: List<InstrumentPreset> ->
        viewModel.update(items)
    }
}