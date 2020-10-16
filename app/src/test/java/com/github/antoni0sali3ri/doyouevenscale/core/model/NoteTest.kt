package com.github.antoni0sali3ri.doyouevenscale.core.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NoteTest {

    @Test
    fun testTranspositionUp() {
        assertEquals(Note.G, Note.C.transpose(7)) {
            "C transposed up by 7 semitones should be G"
        }
        assertEquals(Note.C, Note.A.transpose(3)) {
            "A transposed up by 3 semitones should wrap around to C"
        }
    }

    @Test
    fun testTranspositionDown() {
        assertEquals(Note.Ais, Note.B.transpose(-1)) {
            "B transposed down by 1 semitone should be B flat"
        }
        assertEquals(Note.F, Note.C.transpose(-7)) {
            "C transposed down by 7 semitones should wrap around to F"
        }
    }

    @Test
    fun testTranspositionUpByInterval() {
        assertEquals(Note.Dis, Interval(3).transpose(Note.C)) {
            "C transposed by a minor third should be E flat"
        }
        assertEquals(Note.G, Interval(5).transpose(Note.D)) {
            "G transposed by a perfect fourth should wrap around to D"
        }
    }

    @Test
    fun testTranspositionDownByInterval() {
        assertEquals(Note.Fis, Interval(2).transpose(Note.Gis, false)) {
            "G sharp transposed down by a major second should be F sharp"
        }
        assertEquals(Note.Fis, Interval(6).transpose(Note.C, false)) {
            "C transposed down by a tritone should be G sharp"
        }
    }
}