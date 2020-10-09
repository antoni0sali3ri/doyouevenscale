package com.github.antoni0sali3ri.doyouevenscale.core.model

import kotlin.math.pow

/**
 * A FretSpacing defines the distances between frets.
 *
 * The distances need to be scaled to only show the fretboard, not the entire scale length.
 * The spacings are also relative, such that the open string corresponds to 0.0 and the last fret
 * of a given instrument's fretboard corresponds to 1.0
 */
interface FretSpacing {
    /**
     * Return the relative position for a given fret on an instrument.
     */
    fun getFretPosition(fretNum: Int, fretCount: Int) : Double

    fun getFretPositions(fretCount: Int) = (0..fretCount).map {
        getFretPosition(it, fretCount)
    }
}

/**
 * Constant fret spacing, all frets are the same distance apart.
 */
object ConstantFretSpacing : FretSpacing {
    override fun getFretPosition(fretNum: Int, fretCount: Int): Double {
        return fretNum.toDouble() / fretCount
    }
}

/**
 * Space out the frets according to equal temperament.
 *
 * More realistic than ConstantFretSpacing but may look very cramped with too many frets.
 */
object EqualTemperamentFretSpacing : FretSpacing {

    override fun getFretPosition(fretNum: Int, fretCount: Int): Double {
        val lastFretPosition = spacings[fretCount - 1]
        return spacings[fretNum - 1] / lastFretPosition
    }

    override fun getFretPositions(fretCount: Int): List<Double> {
        val spacingsForInstrument = spacings.subList(0, fretCount + 1)
        val lastFretPosition = spacingsForInstrument.last()
        return spacingsForInstrument.map {
            it / lastFretPosition
        }
    }

    private fun fretSpacing(n: Int) = 1.0 - (1.0 / 2.0.pow(n / 12.0))

    private val spacings by lazy {
        (0..Instrument.MaxFrets).map { fretSpacing(it) }
    }
}