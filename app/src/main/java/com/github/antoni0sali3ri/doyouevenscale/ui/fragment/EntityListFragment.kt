package com.github.antoni0sali3ri.doyouevenscale.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.*
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.EditorActivity
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor.EntityEditorFragment
import com.github.antoni0sali3ri.doyouevenscale.ui.main.ListableEntityRecyclerViewAdapter

sealed class EntityListFragment<T : ListableEntity>(private val clazz: Class<T>) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    protected abstract val viewModel: EntityViewModel<T>

    protected open fun getAdapter(items: List<T>) =
        ListableEntityRecyclerViewAdapter(items, onEditItem, onDeleteItem)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        viewModel.items.observe(viewLifecycleOwner) {
            recyclerView.adapter = getAdapter(it)
        }
        return view
    }

    fun createItem() {
        startActivity(Intent(requireContext(), EditorActivity::class.java).apply {
            putExtra(EditorActivity.ARG_CLASS, clazz)
            putExtra(EntityEditorFragment.ARG_ITEM_ID, 0)
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
    EntityListFragment<InstrumentPreset>(InstrumentPreset::class.java) {

    override fun getAdapter(items: List<InstrumentPreset>) = Adapter(items)

    inner class Adapter(items: List<InstrumentPreset>) :
        ListableEntityRecyclerViewAdapter<InstrumentPreset>(items, onEditItem, onDeleteItem) {

        override val itemLayout: Int = R.layout.list_item_data_manager_draggable
    }

    override val viewModel: PresetViewModel by activityViewModels()
}

class InstrumentListFragment : EntityListFragment<Instrument>(Instrument::class.java) {
    override val viewModel: InstrumentViewModel by activityViewModels()
}

class TuningListFragment : EntityListFragment<Instrument.Tuning>(Instrument.Tuning::class.java) {
    override val viewModel: TuningViewModel by activityViewModels()
    private val instrumentViewModel: InstrumentViewModel by activityViewModels()

    override fun getAdapter(items: List<Instrument.Tuning>): ListableEntityRecyclerViewAdapter<Instrument.Tuning> {
        return Adapter(items)
    }

    inner class Adapter(items: List<Instrument.Tuning>) :
            ListableEntityRecyclerViewAdapter<Instrument.Tuning>(items, onEditItem, onDeleteItem) {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val tuning = items[position]
            val instrument = instrumentViewModel.items.value!!.first { tuning.instrumentId == it.id }
            holder.contentView.text = "${instrument.name} - ${tuning.name}"
        }
    }
}

class ScaleListFragment : EntityListFragment<Scale.Type>(Scale.Type::class.java) {
    override val viewModel: ScaleViewModel by activityViewModels()
}