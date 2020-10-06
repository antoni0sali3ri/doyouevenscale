package de.theopensourceguy.doyouevenscale.ui.view

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Instrument

class FretRangePicker(private val context: Context, root: View, values: IntRange) : NumberPicker.OnValueChangeListener{

    private val numPickMin: NumberPicker = root.findViewById(R.id.numberPickerFretMin)
    private val numPickMax: NumberPicker = root.findViewById(R.id.numberPickerFretMax)

    init {
        numPickMin.minValue = 0
        numPickMin.maxValue = values.last - 1
        numPickMin.value = values.first
        numPickMax.minValue = values.first + 1
        numPickMax.maxValue = Instrument.MaxFrets
        numPickMax.value = values.last
    }

    fun setValues(lower: Int, upper: Int) {
        numPickMin.value = lower
        numPickMax.value = upper
    }

    fun getRange() = numPickMin.value..numPickMax.value

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        when (picker) {
            null -> {}
            numPickMin -> {
                numPickMax.minValue = newVal + 1
            }
            numPickMax -> {
                numPickMin.maxValue = newVal - 1
            }
        }
    }
}