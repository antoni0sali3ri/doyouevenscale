package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.Scale
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor.EntityEditorFragment

class EditorActivity : AppCompatActivity(), EntityEditorFragment.OnCommitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.extras?.apply {
            val clazz = getSerializable(ARG_CLASS) as Class<out ListableEntity>
            val itemId = getLong(EntityEditorFragment.ARG_ITEM_ID)
            val fragment = EntityEditorFragment.newInstance(clazz, itemId)

            title = buildTitle(clazz)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

    private fun buildTitle(entityClass: Class<out ListableEntity>) : String {
        val entityRes = when (entityClass) {
            Instrument::class.java -> R.string.entity_instrument
            InstrumentPreset::class.java -> R.string.entity_preset
            Scale.Type::class.java -> R.string.entity_scale
            Instrument.Tuning::class.java -> R.string.entity_tuning
            else -> throw IllegalArgumentException()
        }
        return String.format(
            resources.getString(R.string.title_activity_editor),
            resources.getString(entityRes)
        )
    }

    companion object {
        val ARG_CLASS = "editor_class"
        val TAG = EditorActivity::class.java.simpleName
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemSaved() {
        Log.d(TAG, "onItemSaved()")
        finish()
    }

    override fun onEditCanceled() {
        Log.d(TAG, "onEditCanceled()")
        finish()
    }
}