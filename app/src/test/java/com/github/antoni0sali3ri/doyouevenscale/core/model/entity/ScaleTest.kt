package com.github.antoni0sali3ri.doyouevenscale.core.model.entity

import com.github.antoni0sali3ri.doyouevenscale.core.model.Interval
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class ScaleTest {

    val scaleTypeName1 = "Super Exotic Scale"
    val scaleTypeName2 = "Another Scale"

    val scaleIntervals1 = listOf(2, 5, 7, 9).map { Interval(it) }
    val scaleIntervals2 = listOf(1, 4, 6, 8).map { Interval(it) }

    val scaleType1 = Scale.Type(scaleIntervals1, scaleTypeName1)
    val scaleType2 = Scale.Type(scaleIntervals2, scaleTypeName2)

    val scale1 = Scale(Note.C, scaleType1)
    val scale2 = Scale(Note.F, scaleType2)

    @Test
    fun testScaleTypeEquality() {
        assertEquals(
            scaleType1,
            Scale.Type(scaleIntervals1, scaleTypeName1)
        )

        assertNotEquals(
            Scale.Type(scaleIntervals1, scaleTypeName1),
            Scale.Type(scaleIntervals1, scaleTypeName2)
        )

        assertNotEquals(
            Scale.Type(scaleIntervals1, scaleTypeName2),
            Scale.Type(scaleIntervals2, scaleTypeName2)
        )
    }

    @Test
    fun testScaleEquality() {
        assertEquals(
            scale1,
            Scale(Note.C, scaleType1)
        )

        assertNotEquals(
            scale1,
            Scale(Note.F, scaleType1)
        )

        assertNotEquals(
            scale1,
            scale2
        )
    }
}