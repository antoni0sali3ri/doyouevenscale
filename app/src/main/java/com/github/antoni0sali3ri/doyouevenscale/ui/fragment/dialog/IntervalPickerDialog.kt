package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.Interval

class IntervalPickerDialog(val listener: ResultListener, val excludeIntervals: List<Interval>) : DialogFragment() {

    interface ResultListener {
        fun onIntervalSelected(interval: Interval)
    }

    private val intervals = INTERVALS.filter { !excludeIntervals.contains(it) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {ctx ->
            val titles = intervals.map {
                ctx.resources.getString(Interval.NAME_RES[it.halfSteps]) + " (${it.halfSteps})" }
            AlertDialog.Builder(ctx)
                .setTitle(R.string.dialog_title_intervals)
                .setItems(titles.toTypedArray()) { _, i ->
                    listener.onIntervalSelected(intervals[i])
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
        } ?: throw IllegalStateException()
    }

    companion object {
        private val INTERVALS = (1..11).map { Interval(it) }.toTypedArray()
    }
}