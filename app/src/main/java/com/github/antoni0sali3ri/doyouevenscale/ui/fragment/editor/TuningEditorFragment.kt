package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note
import com.github.antoni0sali3ri.doyouevenscale.core.model.TuningViewModel
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog.NotePickerDialog

class TuningEditorFragment :
    EntityEditorFragment<Instrument.Tuning>(Instrument.Tuning::class.java),
    NotePickerDialog.ResultListener {

    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var btnAddString: ImageButton
    private var isNew: Boolean = false
    private var displayMode: Note.Display = Note.Display.Sharp
    private var editingIndex = -1

    private val stringPitches: MutableList<Note> = mutableListOf()

    override val layoutResource: Int = R.layout.fragment_tuning_editor

    override val templateItem: Instrument.Tuning = Instrument.Tuning(listOf(Note.C), "Tuning")

    override val viewModel: TuningViewModel by activityViewModels()

    override fun initializeViews(item: Instrument.Tuning) {
        super.initializeViews(item)

        isNew = item.id == 0L
        stringPitches.addAll(item.stringPitches)

        recyclerViewNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NoteListAdapter(stringPitches, this@TuningEditorFragment)
        }
        if (isNew)
            btnAddString.setOnClickListener {
                editingIndex = stringPitches.size
                NotePickerDialog(this, displayMode).show(childFragmentManager, "NotePickerDialog")
            }
        updateAddButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewNotes = view.findViewById(R.id.recyclerViewNotes)
        btnAddString = view.findViewById(R.id.btnAddString)
    }

    fun updateAddButton() {
        btnAddString.visibility = if (isNew && stringPitches.size < Instrument.MaxStrings)
            View.VISIBLE
        else
            View.INVISIBLE

        btnAddString.invalidate()
    }

    class NoteListAdapter(
        private val stringPitches: MutableList<Note>,
        private val parent: TuningEditorFragment
    ) :
        RecyclerView.Adapter<NoteListAdapter.ViewHolder>(),
        NotePickerDialog.ResultListener by parent {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtStringNumber: TextView = view.findViewById(R.id.txtStringNumber)
            val txtStringPitch: TextView = view.findViewById(R.id.txtStringPitch)
            val btnDelete: ImageButton = view.findViewById(R.id.btnRemoveString)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_string_pitches, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtStringNumber.text = (position + 1).toString()
            holder.txtStringPitch.text = stringPitches[position].getName(parent.displayMode)
            holder.txtStringPitch.setOnClickListener {
                parent.showDialog(position)
            }
            if (parent.isNew) {
                holder.btnDelete.setOnClickListener {
                    if (stringPitches.size > 1) {
                        stringPitches.removeAt(position)
                        parent.updateAddButton()
                        notifyDataSetChanged()
                    }
                }
            } else {
                holder.btnDelete.visibility = View.INVISIBLE
            }
        }

        override fun getItemCount(): Int = stringPitches.size
    }

    fun showDialog(index: Int) {
        editingIndex = index
        NotePickerDialog(this, displayMode).show(childFragmentManager, "NotePickerDialog")
    }

    override fun onNoteSelected(note: String, displayMode: Note.Display) {
        if (editingIndex < 0) return

        val n = Note.valueOf(note)
        if (editingIndex >= stringPitches.size)
            stringPitches.add(n)
        else
            stringPitches[editingIndex] = n

        item.setPitches(stringPitches.toList())
        recyclerViewNotes.adapter?.notifyDataSetChanged()
        editingIndex = -1
        this.displayMode = displayMode
        updateAddButton()
    }
}