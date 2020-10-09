package de.theopensourceguy.doyouevenscale.ui.fragment.editor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Interval
import de.theopensourceguy.doyouevenscale.core.model.Scale
import de.theopensourceguy.doyouevenscale.core.model.ScaleViewModel
import de.theopensourceguy.doyouevenscale.ui.fragment.dialog.IntervalPickerDialog

class ScaleEditorFragment : EntityEditorFragment<Scale.Type>(Scale.Type::class.java),
    IntervalPickerDialog.ResultListener {
    private lateinit var recyclerViewIntervals: RecyclerView
    private lateinit var btnAddInterval: ImageButton

    private val intervals: MutableList<Interval> = mutableListOf()

    override val layoutResource: Int = R.layout.fragment_scale_editor

    override val templateItem: Scale.Type = Scale.Type("")

    override val viewModel: ScaleViewModel by activityViewModels()

    override fun initializeViews(item: Scale.Type) {
        super.initializeViews(item)

        intervals.addAll(item.intervals)
        recyclerViewIntervals.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = IntervalListAdapter(requireContext(), intervals)
        }
        btnAddInterval.setOnClickListener {
            IntervalPickerDialog(this, intervals).show(childFragmentManager, "IntervalPickerDialog")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewIntervals = view.findViewById(R.id.recyclerViewIntervals)
        btnAddInterval = view.findViewById(R.id.btnAddInterval)
    }

    override fun onIntervalSelected(interval: Interval) {
        intervals.add(interval)
        intervals.sort()
        item.intervals = intervals.toList()
        recyclerViewIntervals.adapter?.notifyDataSetChanged()
    }

    private fun removeInterval(interval: Interval) {
        intervals.remove(interval)
    }

    class IntervalListAdapter(private val context: Context, val intervals: MutableList<Interval>) :
        RecyclerView.Adapter<IntervalListAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtTitle: TextView = view.findViewById(R.id.txtIntervalName)
            val btnRemove: ImageButton = view.findViewById(R.id.btnRemoveInterval)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_interval, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtTitle.text = context.resources.getString(intervals[position].nameRes)
            holder.btnRemove.setOnClickListener {
                intervals.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int = intervals.size

    }
}