package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note
import com.github.antoni0sali3ri.doyouevenscale.ui.view.NotePickerView

class NotePickerDialog(val listener: ResultListener, val displayMode: Note.Display) : DialogFragment() {

    interface ResultListener {
        fun onNoteSelected(note: Note, displayMode: Note.Display)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val view = NotePickerView(it, displayMode)
            view.createChildren(it.layoutInflater) {
                val note = Note.valueOf((it as TextView).tag.toString())
                listener.onNoteSelected(note, view.displayMode)
                this.dismiss()
            }

            AlertDialog.Builder(it)
                .setTitle(R.string.dialog_title_note_picker)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, {_,_ -> })
                .create()
        } ?: throw IllegalStateException()
    }
}