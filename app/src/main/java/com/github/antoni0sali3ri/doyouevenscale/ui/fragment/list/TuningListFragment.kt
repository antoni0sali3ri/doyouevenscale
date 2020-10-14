package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import androidx.fragment.app.viewModels
import com.github.antoni0sali3ri.doyouevenscale.core.model.InstrumentViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.TuningViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.ListableEntityRecyclerViewAdapter

class TuningListFragment : EntityListFragment<Instrument.Tuning>(Instrument.Tuning::class.java) {
    override val viewModel: TuningViewModel by viewModels()
    private val instrumentViewModel: InstrumentViewModel by viewModels()
    private lateinit var adapter: Adapter
    private val instruments: MutableList<Instrument> = mutableListOf()

    override fun getAdapter(): ListableEntityRecyclerViewAdapter<Instrument.Tuning> {
        val adapter = Adapter(emptyList())
        this.adapter = adapter
        return adapter
    }

    override fun setItems(items: List<Instrument.Tuning>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    override fun populateRecyclerView() {
        instrumentViewModel.items.observe(viewLifecycleOwner) {
            instruments.clear()
            instruments.addAll(it)
            super.populateRecyclerView()
        }
    }

    inner class Adapter(items: List<Instrument.Tuning>) :
        ListableEntityRecyclerViewAdapter<Instrument.Tuning>(items, onEditItem, onDeleteItem) {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val tuning = items[position]
            val instrument = instruments.firstOrNull { it.id == tuning.instrumentId }
            holder.contentView.text = "${instrument?.name ?: "Unknown"} - ${tuning.name}"
        }
    }
}