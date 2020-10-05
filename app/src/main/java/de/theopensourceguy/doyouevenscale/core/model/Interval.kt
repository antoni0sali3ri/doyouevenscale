package de.theopensourceguy.doyouevenscale.core.model

import de.theopensourceguy.doyouevenscale.R

data class Interval(
    val halfSteps: Int
) {
    init {
        require(halfSteps >= 0)
    }
    val nameRes = nameResFor(halfSteps)

    fun shift(note: Note, shiftUp: Boolean = true) : Note = note.shift(if (shiftUp) halfSteps else -halfSteps)

    companion object {
        val NAME_RES = arrayOf(
            R.string.intervalPrime,
            R.string.intervalMinSecond,
            R.string.intervalMajSecond,
            R.string.intervalMinThird,
            R.string.intervalMajThird,
            R.string.intervalFourth,
            R.string.intervalTritone,
            R.string.intervalFifth,
            R.string.intervalMinSixth,
            R.string.intervalMajSixth,
            R.string.intervalMinSeventh,
            R.string.intervalMajSeventh
        )

        fun nameResFor(halfSteps: Int) = NAME_RES[halfSteps % 12]
    }
}

fun Int.toInterval() : Interval {
    return Interval(this)
}