package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.graphics.Point
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument

class Fingering private constructor(val stringNo: Int, val fretNo: Int) {

    fun toPoint(): Point = Point(stringNo, fretNo)

    override fun equals(other: Any?): Boolean {
        return other is Fingering
                && stringNo == other.stringNo
                && fretNo == other.fretNo
    }

    override fun hashCode(): Int = 31 * stringNo + fretNo

    companion object {
        val Fingerings: Array<Array<Fingering>> by lazy {
            val result = Array(Instrument.MaxStrings) { s ->
                Array(Instrument.MaxFrets + 1) { f ->
                    Fingering(s + 1, f)
                }
            }
            result
        }

        operator fun invoke(stringNo: Int, fretNo: Int): Fingering {
            require(stringNo in 1..Instrument.MaxStrings)
            require(fretNo in 0..Instrument.MaxFrets)
            return Fingerings[stringNo - 1][fretNo]
        }
    }
}