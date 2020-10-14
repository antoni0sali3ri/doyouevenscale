package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import androidx.fragment.app.viewModels
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

    private val itemTouchHelper: ItemTouchHelper
    private val adapter: InstrumentPresetRecyclerViewAdapter

    private val onUpdateItems: (List<InstrumentPreset>) -> Unit = { items: List<InstrumentPreset> ->
        viewModel.update(items)
    }

    init {
        adapter = InstrumentPresetRecyclerViewAdapter(
            emptyList(),
            this,
            onUpdateItems,
            onEditItem,
            onDeleteItem
        )
        rvAdapter = adapter
        val callback = PresetListItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
    }

    override val viewModel: PresetViewModel by viewModels()

    override fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun setItems(items: List<InstrumentPreset>) {
        adapter.items = items.sortedWith(InstrumentPresetComparator)
        adapter.notifyDataSetChanged()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

}