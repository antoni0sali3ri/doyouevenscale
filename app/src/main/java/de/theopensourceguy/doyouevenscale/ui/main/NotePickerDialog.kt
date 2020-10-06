package de.theopensourceguy.doyouevenscale.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Note

class NotePickerDialog(val listener: ResultListener, val displayMode: Note.Display) : DialogFragment() {

    interface ResultListener {
        fun onNoteSelected(note: String, displayMode: Note.Display)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val view = NotePickerView(it, displayMode)
            view.createChildren(it.layoutInflater) {
                listener.onNoteSelected((it as TextView).tag.toString(), view.displayMode)
                this.dismiss()
            }

            val builder = AlertDialog.Builder(it)
                .setTitle(R.string.dialogNotePickerTitle)
                .setView(view)
            builder.create()
        } ?: throw IllegalStateException()
    }
}