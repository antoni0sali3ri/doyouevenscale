package com.github.antoni0sali3ri.doyouevenscale.core.model

import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale

/**
 * An instrument with a tuning applied.
 *
 * Provides several convenience methods to find the fretboard positions corresponding to a note or scale.
 */
data class TunedInstrument(
    val instrument: Instrument,
    val tuning: Instrument.Tuning
) {
    init {
        require(instrument.id == tuning.instrumentId)
    }

    /**
     * Get the note at the specified string and fret.
     *
     * @param stringNo string number (starts at 1)
     * @param fretNo fret number (starts at 0, which is the open string)
     */
    fun getNoteAt(stringNo: Int, fretNo: Int): Note {
        require(stringNo > 0 && stringNo <= instrument.stringCount)
        require(fretNo >= 0 && fretNo <= Instrument.MaxFrets)
        return tuning.pitchOf(stringNo).transpose(fretNo)
    }

    fun getFretsForScale(
        scale: Scale,
        fretsShown: IntRange = 0..Instrument.MaxFrets
    ): List<Fingering> {
        val positions: MutableList<Fingering> = mutableListOf()
        for (stringNo in 1..instrument.stringCount) {
            positions.addAll(
                getFretsForScale(stringNo, scale, fretsShown).map {
                    Fingering(stringNo, it - fretsShown.first)
                    //Point(stringNo, it - fretsShown.first)
                }
            )
        }
        return positions.toList()
    }


    fun getFretsForScale(
        stringNo: Int,
        scale: Scale,
        fretsShown: IntRange = 0..Instrument.MaxFrets
    ): List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return fretsShown.filter {
            scale.contains(openString.transpose(it))
        }
    }

    fun getRootsForString(stringNo: Int, root: Note, fretsShown: IntRange): List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return fretsShown.filter {
            openString.transpose(it) == root
        }
    }

    fun getRootsForString(stringNo: Int, scale: Scale, fretsShown: IntRange): List<Int> =
        getRootsForString(stringNo, scale.root, fretsShown)

    fun getRoots(root: Note, fretsShown: IntRange): List<Fingering> {
        val positions: MutableList<Fingering> = mutableListOf()
        for (stringNo in 1..instrument.stringCount) {
            positions.addAll(
                getRootsForString(stringNo, root, fretsShown).map {
                    Fingering(stringNo, it - fretsShown.first)
                    //Point(stringNo, it - fretsShown.first)
                }
            )
        }
        return positions.toList()
    }

    fun getRoots(scale: Scale, fretsShown: IntRange): List<Fingering> =
        getRoots(scale.root, fretsShown)
}
