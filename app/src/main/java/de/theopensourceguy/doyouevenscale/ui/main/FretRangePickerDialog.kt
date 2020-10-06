package de.theopensourceguy.doyouevenscale.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import de.theopensourceguy.doyouevenscale.R

class FretRangePickerDialog(val range: IntRange, val listener: ResultListener) : DialogFragment() {

    private lateinit var rangePicker: FretRangePicker

    interface ResultListener {
        fun updateFretRange(range: IntRange)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val view = it.layoutInflater.inflate(R.layout.layout_fret_range_picker, null, false)
            rangePicker = FretRangePicker(it.applicationContext, view, range)

            val builder = AlertDialog.Builder(it)

            builder
                .setTitle(R.string.dialogFretRangeTitle)
                .setView(view)
                .setPositiveButton(android.R.string.ok, { di, i ->
                    listener.updateFretRange(rangePicker.getRange())
                })
                .setNegativeButton(android.R.string.cancel, { di, i ->

                })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}