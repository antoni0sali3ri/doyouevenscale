package com.github.antoni0sali3ri.doyouevenscale.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.*
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.EditorActivity
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor.EntityEditorFragment
import com.github.antoni0sali3ri.doyouevenscale.ui.main.*

sealed class EntityListFragment<T : ListableEntity>(private val clazz: Class<T>) : Fragment() {

    protected lateinit var recyclerView: RecyclerView
    protected abstract val viewModel: EntityViewModel<T>
    protected lateinit var adapterWithItems: HasItems<T>

    protected open fun getAdapter(items: List<T>): RecyclerView.Adapter<*> {
        val adapter = ListableEntityRecyclerViewAdapter(items, onEditItem, onDeleteItem)
        adapterWithItems = adapter
        return adapter
    }

    protected open fun setItems(items: List<T>) {
        adapterWithItems.items = items
        recyclerView.adapter?.notifyDataSetChanged()
    }

    protected open fun setUpRecyclerView(recyclerView: RecyclerView, items: List<T>) {
        recyclerView.adapter = getAdapter(items)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entity_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            with(view) {
                layoutManager = LinearLayoutManager(context)
                val deco = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                //deco.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)!!)
                addItemDecoration(deco)
            }
        }
        setUpRecyclerView(recyclerView, emptyList())
        viewModel.items.observe(viewLifecycleOwner) { items ->
            lifecycleScope.launchWhenStarted {
                setItems(items)
            }
        }
        return view
    }

    fun createItem() {
        startActivity(Intent(requireContext(), EditorActivity::class.java).apply {
            putExtra(EditorActivity.ARG_CLASS, clazz)
            putExtra(EntityEditorFragment.ARG_ITEM_ID, 0L)
        })
    }

    protected val onEditItem = { id: Long ->
        startActivity(Intent(requireContext(), EditorActivity::class.java).apply {
            putExtra(EditorActivity.ARG_CLASS, clazz)
            putExtra(EntityEditorFragment.ARG_ITEM_ID, id)
        })
    }

    protected val onDeleteItem: (Long) -> Unit = { id: Long ->
        viewModel.delete(id)
    }
}

class InstrumentPresetListFragment :
    EntityListFragment<InstrumentPreset>(InstrumentPreset::class.java), OnStartDragListener {

    private lateinit var itemTouchHelper: ItemTouchHelper

    private val onUpdateItems: (List<InstrumentPreset>) -> Unit = { items: List<InstrumentPreset> ->
        viewModel.update(items)
    }

    override fun setUpRecyclerView(recyclerView: RecyclerView, items: List<InstrumentPreset>) {
        val adapter = InstrumentPresetListAdapter(
            items.toMutableList(),
            this,
            onUpdateItems,
            onEditItem,
            onDeleteItem
        )
        adapterWithItems = adapter
        recyclerView.adapter = adapter
        val callback = PresetListItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun setItems(items: List<InstrumentPreset>) {
        super.setItems(items.sortedWith(InstrumentPresetComparator))
        (adapterWithItems as InstrumentPresetListAdapter).apply {
            itemsMutable.clear()
            itemsMutable.addAll(items.sortedWith(InstrumentPresetComparator))
        }
    }

    override val viewModel: PresetViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

}


class InstrumentListFragment : EntityListFragment<Instrument>(Instrument::class.java) {
    override val viewModel: InstrumentViewModel by activityViewModels()
}

class TuningListFragment : EntityListFragment<Instrument.Tuning>(Instrument.Tuning::class.java) {
    override val viewModel: TuningViewModel by activityViewModels()
    private val instrumentViewModel: InstrumentViewModel by activityViewModels()

    override fun getAdapter(items: List<Instrument.Tuning>): ListableEntityRecyclerViewAdapter<Instrument.Tuning> {
        val adapter = Adapter(items)
        adapterWithItems = adapter
        return adapter
    }

    inner class Adapter(items: List<Instrument.Tuning>) :
        ListableEntityRecyclerViewAdapter<Instrument.Tuning>(items, onEditItem, onDeleteItem) {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val tuning = items[position]
            instrumentViewModel.items.observe(viewLifecycleOwner) { instruments ->
                val instrument = instruments.first { tuning.instrumentId == it.id }
                holder.contentView.text = "${instrument.name} - ${tuning.name}"
            }
        }
    }
}

class ScaleListFragment : EntityListFragment<Scale.Type>(Scale.Type::class.java) {
    override val viewModel: ScaleViewModel by activityViewModels()
}