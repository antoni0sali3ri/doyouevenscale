package com.github.antoni0sali3ri.doyouevenscale.core.model

import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TunedInstrumentTest {

    private val Guitar = Instrument(6, "Guitar").apply { id = 1 }
    private val Mandolin = Instrument(4, "Mandolin").apply { id = 2 }

    private val GuitarStandardTuning = Instrument.Tuning(
        Guitar.id,
        listOf(Note.E, Note.B, Note.G, Note.D, Note.A, Note.E),
        "Standard Tuning"
    )
    private val MandolinTuning =
        Instrument.Tuning(Mandolin.id, listOf(Note.E, Note.A, Note.D, Note.G), "Standard Tuning")

    private val TunedGuitar = TunedInstrument(Guitar, GuitarStandardTuning)
    private val TunedMando = TunedInstrument(Mandolin, MandolinTuning)

    private val MinorPentatonicG = Scale(Note.G, Scale.Type("Minor Penta", 3, 5, 7, 10))

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun testConstructor() {
        assertThrows(IllegalArgumentException::class.java) {
            TunedInstrument(Guitar, MandolinTuning)
        }
    }

    @Test
    fun testGetFingering() {
        assertEquals(
            Note.C,
            TunedGuitar.getNoteAt(Fingering(5, 3))
        )

        assertEquals(
            Note.F,
            TunedGuitar.getNoteAt(Fingering(2, 6))
        )

        assertEquals(
            Note.Ais,
            TunedMando.getNoteAt(Fingering(2, 13))
        )
    }

    @Test
    fun testGetRootsForString() {
        val range = 0..12
        assertEquals(
            listOf(0, 12),
            TunedGuitar.getRootsForString(6, Note.E, range)
        )

        assertEquals(
            listOf(7),
            TunedMando.getRootsForString(3, Note.A, range)
        )
    }

    @Test
    fun testGetRoots() {
        val range = 0..24
        val expectedFingerings = listOf(
            Fingering(1, 3),
            Fingering(1, 15),
            Fingering(2, 8),
            Fingering(2, 20),
            Fingering(3, 0),
            Fingering(3, 12),
            Fingering(3, 24),
            Fingering(4, 5),
            Fingering(4, 17),
            Fingering(5, 10),
            Fingering(5, 22),
            Fingering(6, 3),
            Fingering(6, 15)
        )
        val actualFingerings = TunedGuitar.getRoots(MinorPentatonicG, range)
        assert(
            expectedFingerings.containsAll(actualFingerings)
                    && actualFingerings.containsAll(expectedFingerings)
        )
    }

    @Test
    fun testGetFingeringsForScale() {
        val range = 0..5
        val expectedFingerings = listOf(
            Fingering(4, 0),
            Fingering(4, 3),
            Fingering(4, 5),
            Fingering(3, 0),
            Fingering(3, 3),
            Fingering(3, 5),
            Fingering(2, 1),
            Fingering(2, 3),
            Fingering(2, 5),
            Fingering(1, 1),
            Fingering(1, 3)
        )
        val actualFingerings = TunedMando.getFretsForScale(MinorPentatonicG, range)
        assert(
            expectedFingerings.containsAll(actualFingerings)
                    && actualFingerings.containsAll(expectedFingerings)
        )
    }
}