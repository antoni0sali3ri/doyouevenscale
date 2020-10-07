package de.theopensourceguy.doyouevenscale.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Note
import de.theopensourceguy.doyouevenscale.ui.view.NotePickerView

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

            AlertDialog.Builder(it)
                .setTitle(R.string.dialog_title_note_picker)
                .setView(view)
                .create()
        } ?: throw IllegalStateException()
    }
}