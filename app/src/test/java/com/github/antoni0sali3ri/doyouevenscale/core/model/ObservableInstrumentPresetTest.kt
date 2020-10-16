package com.github.antoni0sali3ri.doyouevenscale.core.model

import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ObservableInstrumentPresetTest {

    private lateinit var changeListener: ChangeListener
    private lateinit var preset: ObservableInstrumentPreset

    @BeforeEach
    fun setUp() {
        changeListener = ChangeListener()
        preset = ObservableInstrumentPreset(
            Guitar,
            StandardTuning,
            Note.C,
            MajorPentatonic,
            0..12,
            Note.Display.Sharp
        )
        preset.listeners.add(changeListener)
    }

    @Test
    fun testChangeTuning() {
        preset.tuning = OpenDTuning
        assertEquals(OpenDTuning, changeListener.newTuning)
        assertEquals(StandardTuning, changeListener.oldTuning)
    }

    @Test
    fun testChangeRoot() {
        preset.rootNote = Note.Fis
        assertEquals(Note.Fis, changeListener.newScale?.root)
        assertEquals(Note.C, changeListener.oldScale?.root)
    }

    @Test
    fun testChangeScaleType() {
        preset.scaleType = MinorPentatonic
        assertEquals(MinorPentatonic, changeListener.newScale?.type)
        assertEquals(MajorPentatonic, changeListener.oldScale?.type)
    }

    @Test
    fun testChangeFretRange() {
        val range = 2..7
        preset.fretsShown = range
        assertEquals(range, changeListener.newRange)
        assertEquals(0..12, changeListener.oldRange)
    }

    @Test
    fun testChangeNoteDisplay() {
        preset.noteDisplay = Note.Display.Flat
        assertEquals(Note.Display.Flat, changeListener.newDisplay)
        assertEquals(Note.Display.Sharp, changeListener.oldDisplay)
    }

    private inner class ChangeListener : ObservableInstrumentPreset.OnChangeListener {
        var newTuning: Instrument.Tuning? = null
        var oldTuning: Instrument.Tuning? = null
        var newScale: Scale? = null
        var oldScale: Scale? = null
        var newRange: IntRange? = null
        var oldRange: IntRange? = null
        var newDisplay: Note.Display? = null
        var oldDisplay: Note.Display? = null

        override fun onTuningChanged(newTuning: Instrument.Tuning, oldTuning: Instrument.Tuning) {
            this.oldTuning = oldTuning
            this.newTuning = newTuning
        }

        override fun onScaleChanged(newScale: Scale, oldScale: Scale) {
            this.oldScale = oldScale
            this.newScale = newScale
        }

        override fun onFretRangeChanged(newRange: IntRange, oldRange: IntRange) {
            this.oldRange = oldRange
            this.newRange = newRange
        }

        override fun onNoteDisplayChanged(newDisplay: Note.Display, oldDisplay: Note.Display) {
            this.oldDisplay = oldDisplay
            this.newDisplay = newDisplay
        }
    }

    companion object {
        val Guitar = Instrument(6, "Guitar").apply { id = 1 }

        val StandardTuning = Instrument.Tuning(
            Guitar.id,
            listOf(Note.E, Note.A, Note.D, Note.G, Note.B, Note.E),
            "Standard Tuning"
        )
        val OpenDTuning = Instrument.Tuning(
            Guitar.id,
            listOf(Note.D, Note.A, Note.D, Note.Fis, Note.A, Note.D),
            "Open D"
        )

        val MajorPentatonic = Scale.Type("Major Pentatonic", 2, 4, 7, 9)
        val MinorPentatonic = Scale.Type("Minor Pentatonic", 3, 5, 7, 10)
    }
}