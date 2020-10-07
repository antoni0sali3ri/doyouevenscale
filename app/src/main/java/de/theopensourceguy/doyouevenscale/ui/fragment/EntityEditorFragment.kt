package de.theopensourceguy.doyouevenscale.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.theopensourceguy.doyouevenscale.MyApp
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.db.ViewModelDao
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.ListableEntity
import de.theopensourceguy.doyouevenscale.core.model.Note
import de.theopensourceguy.doyouevenscale.core.model.Scale

sealed class EntityEditorFragment<T : ListableEntity>(val clazz: Class<T>) : Fragment() {

    protected abstract val layoutResource: Int
    protected abstract val templateItem: T

    protected lateinit var item: T

    protected lateinit var btnSubmit: Button
    protected lateinit var btnCancel: Button
    protected lateinit var edtEntityName: EditText

    private lateinit var dao : ViewModelDao<T>
    private lateinit var commitListener: OnCommitListener

    protected fun commitChanges() {
        if (item.id == 0L)
            dao.insertSingle(item)
        else
            dao.updateSingle(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnCommitListener) {
            commitListener = context
        } else {
            throw ClassCastException("Attached context needs to implement OnCommitListener")
        }

        dao = MyApp.getDatabase(requireContext()).getDaoForClass(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemId = requireArguments().getLong(ARG_ITEM_ID)

        item = if (itemId > 0) dao.getSingle(itemId) else templateItem
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(layoutResource, container, false)
        btnSubmit = root.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            commitChanges()
            commitListener.onItemSaved()
        }

        btnCancel = root.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            commitListener.onEditCanceled()
        }

        edtEntityName = root.findViewById(R.id.editEntityName)
        edtEntityName.setText(item.name, TextView.BufferType.EDITABLE)
        edtEntityName.addTextChangedListener(entityNameTextWatcher)
        return root
    }

    val entityNameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                item.name = s.toString()
            }
        }

    }

    interface OnCommitListener {
        fun onItemSaved()

        fun onEditCanceled()
    }

    companion object {
        val ARG_ITEM_ID: String = "entity_item_id"

        @JvmStatic
        fun <T : ListableEntity> newInstance(clazz: Class<T>, itemId: Long = 0) : Fragment {
            val fragment = when(clazz) {
                Instrument::class.java -> InstrumentEditorFragment()
                Instrument.Tuning::class.java -> TuningEditorFragment()
                Scale.Type::class.java -> ScaleEditorFragment()
                else -> throw IllegalArgumentException()
            }
            return fragment.apply {
                arguments = Bundle().apply {
                    putLong(ARG_ITEM_ID, itemId)
                }
            }
        }
    }
}

class InstrumentEditorFragment : EntityEditorFragment<Instrument>(Instrument::class.java) {
    override val layoutResource: Int = R.layout.fragment_instrument_editor

    override val templateItem: Instrument = Instrument(4, "Instrument")

    private lateinit var npStringCount: NumberPicker



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        npStringCount = view.findViewById(R.id.numberPickerStringCount)
        npStringCount.minValue = 1
        npStringCount.maxValue = Instrument.MaxStrings
        npStringCount.value = item.numStrings
        npStringCount.setOnValueChangedListener { picker, oldVal, newVal ->
            item.numStrings = newVal
        }
    }

}

class TuningEditorFragment : EntityEditorFragment<Instrument.Tuning>(Instrument.Tuning::class.java) {
    override val layoutResource: Int = R.layout.fragment_tuning_editor

    override val templateItem: Instrument.Tuning = Instrument.Tuning(listOf(Note.C), "Tuning")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit.isEnabled = false
    }
}

class ScaleEditorFragment : EntityEditorFragment<Scale.Type>(Scale.Type::class.java) {
    override val layoutResource: Int = R.layout.fragment_scale_editor

    override val templateItem: Scale.Type = Scale.Type("Scale", 2, 3)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit.isEnabled = false
    }
}