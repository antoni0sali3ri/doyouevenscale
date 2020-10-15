package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor.EntityEditorFragment

class EditorActivity : AppCompatActivity(), EntityEditorFragment.OnCommitListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        applyOrientation()

        intent.extras?.apply {
            @SuppressWarnings("unchecked")
            val clazz = getSerializable(ARG_CLASS) as Class<out ListableEntity>
            val itemId = getLong(EntityEditorFragment.ARG_ITEM_ID)
            val fragment = EntityEditorFragment.newInstance(clazz, itemId)

            title = buildTitle(clazz, if (itemId == 0L) Mode.Create else Mode.Edit)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

    private fun buildTitle(entityClass: Class<out ListableEntity>, mode: Mode): String {
        val entityRes = when (entityClass) {
            Instrument::class.java -> R.string.entity_instrument
            InstrumentPreset::class.java -> R.string.entity_preset
            Scale.Type::class.java -> R.string.entity_scale
            Instrument.Tuning::class.java -> R.string.entity_tuning
            else -> throw IllegalArgumentException()
        }
        return String.format(
            resources.getString(mode.stringRes),
            resources.getString(entityRes)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemSaved() {
        finish()
    }

    override fun onEditCanceled() {
        finish()
    }

    companion object {
        val ARG_CLASS = "editor_class"

        fun launch(
            fromActivity: Activity,
            forClass: Class<out ListableEntity>,
            itemToEdit: Long = 0L
        ) {
            ContextCompat.startActivity(
                fromActivity,
                Intent(fromActivity, EditorActivity::class.java).apply {
                    putExtra(ARG_CLASS, forClass)
                    putExtra(EntityEditorFragment.ARG_ITEM_ID, itemToEdit)
                },
                null
            )
        }
    }

    enum class Mode(val stringRes: Int) {
        Create(R.string.title_activity_editor_create), Edit(R.string.title_activity_editor_edit)
    }
}