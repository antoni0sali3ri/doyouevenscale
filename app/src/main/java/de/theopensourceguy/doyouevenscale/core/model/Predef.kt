package de.theopensourceguy.doyouevenscale.core.model

object Predef {

    object Instruments {
        val Guitar = Instrument(6, 24, Tunings.GuitarStandard)
        val Mandolin = Instrument(4, 24, Tunings.Violin)
    }

    object Tunings {
        val GuitarStandard = Tuning(Note.E, Note.A, Note.D, Note.G, Note.B, Note.E)
        val GuitarOpenD = Tuning(Note.D, Note.A, Note.D, Note.Fis, Note.A, Note.D)
        val Violin = Tuning(Note.G, Note.D, Note.A, Note.E)
        val BanjoOpenG = Tuning(Note.G, Note.D, Note.G, Note.B, Note.D)
        val BanjoDoubleC = Tuning(Note.G, Note.C, Note.G, Note.C, Note.D)
    }

    object ScaleTypes {
        val Ionian = ScaleType(2, 4, 5, 7, 9, 11)
        val Dorian = ScaleType(2, 3, 5, 7, 9, 10)
        val Phrygian = ScaleType(1, 3, 5, 7, 8, 10)
        val Lydian = ScaleType(2, 4, 6, 7, 9, 11)
        val Mixolydian = ScaleType(2, 4, 5, 7, 9, 10)
        val Aeolian = ScaleType(2, 3, 5, 7, 8, 10)
        val Locrian = ScaleType(1, 3, 5, 6, 8, 10)
    }
}