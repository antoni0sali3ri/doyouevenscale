package de.theopensourceguy.doyouevenscale.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.ui.view.FretRangePicker

class FretRangePickerDialog(val range: IntRange, val listener: ResultListener) : DialogFragment() {

    private lateinit var rangePicker: FretRangePicker

    interface ResultListener {
        fun updateFretRange(range: IntRange)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.layout_fret_range_picker, null, false)
            rangePicker = FretRangePicker(it, view, range)

            AlertDialog.Builder(it)
                .setTitle(R.string.dialog_title_fret_range)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { di, i ->
                    listener.updateFretRange(rangePicker.getRange())
                }
                .setNegativeButton(android.R.string.cancel) { di, i ->

                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}