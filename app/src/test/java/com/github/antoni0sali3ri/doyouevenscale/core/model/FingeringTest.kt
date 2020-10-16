package com.github.antoni0sali3ri.doyouevenscale.core.model

import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FingeringTest {

    private val validFret = 0
    private val validString = 1

    @Test
    fun testConstructor() {
        assertThrows(IllegalArgumentException::class.java) {
            Fingering(validString, -10)
        }

        assertThrows(IllegalArgumentException::class.java) {
            Fingering(validString, Instrument.MaxFrets + 1)
        }

        assertThrows(IllegalArgumentException::class.java) {
            Fingering(0, validFret)
        }

        assertThrows(IllegalArgumentException::class.java) {
            Fingering(Instrument.MaxStrings + 1, validFret)
        }
    }

    @Test
    fun testEquality() {
        assertEquals(
            Fingering(1, 10),
            Fingering(1, 10),
        )

        assertNotEquals(
            Fingering(5, 20),
            Fingering(1, 20)
        )

        assertNotEquals(
            Fingering(1, 10),
            Fingering(1, 12)
        )
    }
}