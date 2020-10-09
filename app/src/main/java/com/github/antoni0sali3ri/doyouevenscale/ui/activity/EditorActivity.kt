package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor.EntityEditorFragment

class EditorActivity : AppCompatActivity(), EntityEditorFragment.OnCommitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        intent.extras?.apply {
            val clazz = getSerializable(ARG_CLASS) as Class<out ListableEntity>
            val itemId = getLong(EntityEditorFragment.ARG_ITEM_ID)
            val fragment = EntityEditorFragment.newInstance(clazz, itemId)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

    companion object {
        val ARG_CLASS = "editor_class"
        val TAG = EditorActivity::class.java.simpleName
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