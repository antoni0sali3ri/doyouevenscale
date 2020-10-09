package de.theopensourceguy.doyouevenscale.ui.fragment.editor

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ca.allanwang.kau.utils.launchMain
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class EntityEditorFragment<T : ListableEntity>(val clazz: Class<T>) : Fragment() {

    protected abstract val layoutResource: Int
    protected abstract val templateItem: T

    protected lateinit var item: T

    protected abstract val viewModel: EntityViewModel<T>

    protected lateinit var btnSubmit: Button
    protected lateinit var btnCancel: Button
    protected lateinit var edtEntityName: EditText

    private lateinit var commitListener: OnCommitListener

    protected fun commitChanges() {
        if (item.id == 0L)
            viewModel.insert(item)
        else
            viewModel.update(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnCommitListener) {
            commitListener = context
        } else {
            throw ClassCastException("Attached context needs to implement OnCommitListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemId = requireArguments().getLong(ARG_ITEM_ID)

        lifecycleScope.launch(Dispatchers.IO) {
            val item = if (itemId > 0) viewModel.getSingle(itemId) else templateItem
            launchMain {
                initializeViews(item)
            }
        }
    }

    protected open fun initializeViews(item: T) {
        this.item = item
        edtEntityName.setText(item.name, TextView.BufferType.EDITABLE)

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
        const val ARG_ITEM_ID: String = "entity_item_id"

        @JvmStatic
        fun <T : ListableEntity> newInstance(clazz: Class<T>, itemId: Long = 0): Fragment {
            val fragment = when (clazz) {
                InstrumentPreset::class.java -> InstrumentPresetEditorFragment()
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

