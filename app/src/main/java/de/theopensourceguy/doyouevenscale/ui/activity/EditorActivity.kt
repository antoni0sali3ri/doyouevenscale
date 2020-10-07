package de.theopensourceguy.doyouevenscale.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.ListableEntity
import de.theopensourceguy.doyouevenscale.ui.fragment.EntityEditorFragment

class EditorActivity : AppCompatActivity() {
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
    }
}