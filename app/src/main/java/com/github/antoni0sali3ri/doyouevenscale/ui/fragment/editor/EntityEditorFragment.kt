package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ca.allanwang.kau.utils.launchMain
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.EntityViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
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

    protected fun showValidationMessage(messageRes: Int) {
        Toast.makeText(requireContext(), messageRes, Toast.LENGTH_LONG).show()
    }

    protected fun showValidationMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected open fun validateItem(): Boolean {
        if (item.name.isBlank()) {
            showValidationMessage(R.string.msg_entity_name_is_blank)
            return false
        }
        return true
    }

    private fun commitChanges() {
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
            if (validateItem()) {
                commitChanges()
                commitListener.onItemSaved()
            }
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

