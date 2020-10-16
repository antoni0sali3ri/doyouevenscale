package com.github.antoni0sali3ri.doyouevenscale.core.model.entity

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class InstrumentTest {

    @Test
    fun testConstructor() {
        assertThrows(IllegalArgumentException::class.java) {
            Instrument(0, "")
        }

        assertThrows(IllegalArgumentException::class.java) {
            Instrument(Instrument.MaxStrings + 1, "")
        }
    }
}