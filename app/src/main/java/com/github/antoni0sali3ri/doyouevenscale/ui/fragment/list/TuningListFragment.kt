package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import androidx.fragment.app.activityViewModels
import com.github.antoni0sali3ri.doyouevenscale.core.model.InstrumentViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.TuningViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.ListableEntityRecyclerViewAdapter

class TuningListFragment : EntityListFragment<Instrument.Tuning>(Instrument.Tuning::class.java) {
    override val viewModel: TuningViewModel by activityViewModels()
    private val instrumentViewModel: InstrumentViewModel by activityViewModels()
    private lateinit var adapter: Adapter

    override fun getAdapter(items: List<Instrument.Tuning>): ListableEntityRecyclerViewAdapter<Instrument.Tuning> {
        val adapter = Adapter(items)
        this.adapter = adapter
        return adapter
    }

    override fun setItems(items: List<Instrument.Tuning>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
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