package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.children
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Note
import kotlinx.android.synthetic.main.layout_note_display_mode.view.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class NotePickerView(context: Context, _displayMode: Note.Display) : ViewGroup(context) {

    private var initialized = false
    var displayMode: Note.Display = _displayMode
        set(value) {
            if (field != value) {
                updateViews(value)
            }
            field = value
        }

    private lateinit var textViews: List<TextView>
    private lateinit var displayModeToggle: ToggleButton

    private fun updateViews(displayMode: Note.Display) {
        if (!initialized) return

        for (i in 0 until 12) {
            textViews[i].text = Note.CircleOfFifths[i].getName(displayMode)
        }
    }

    fun createChildren(inflater: LayoutInflater, onClick: OnClickListener) {
        if (initialized) return

        val views: MutableList<TextView> = mutableListOf()

        for (i in 0 until 12)
            inflater.inflate(R.layout.layout_note_text_view, this, true)

        textViews = children
            .filter { it is TextView }
            .map { it as TextView }
            .toList()

        for (i in 0 until 12) {
            val textView = textViews[i]
            val note = Note.CircleOfFifths[i]
            textView.text = note.getName(displayMode)
            textView.tag = note.toString()
            textView.setOnClickListener(onClick)
            views.add(textView)
        }

        inflater.inflate(R.layout.layout_note_display_mode, this, true)
        displayModeToggle = children.first { it is ToggleButton } as ToggleButton
        displayModeToggle.isChecked = displayMode == Note.Display.Sharp
        displayModeToggle.setOnClickListener {
            displayMode = if (displayModeToggle.isChecked) Note.Display.Sharp else Note.Display.Flat
        }

        initialized = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size, size)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (initialized && changed) {
            val increment = PI / 6
            var angle = -PI / 2
            val centerX = (r - l) / 2.0
            val centerY = (b - t) / 2.0
            val radius = 0.8 * min(centerX, centerY)
            var cx: Double
            var cy: Double
            textViews.forEach {
                cx = centerX + cos(angle) * radius
                cy = centerY + sin(angle) * radius
                it.apply {
                    val tvSize = textSize
                    layout(
                        (cx - tvSize).toInt(), (cy - tvSize * 0.75).toInt(),
                        (cx + tvSize).toInt(), (cy + tvSize * 0.75).toInt()
                    )
                }
                angle += increment
            }
            val buttonSize = toggleButton.textSize
            toggleButton.layout(
                (centerX - buttonSize).toInt(), (centerY - buttonSize).toInt(),
                (centerX + buttonSize).toInt(), (centerY + buttonSize).toInt()
            )
        }
    }

}