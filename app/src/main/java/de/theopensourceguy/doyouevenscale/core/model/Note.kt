package de.theopensourceguy.doyouevenscale.core.model

enum class Note(val nameSharp: String, val nameFlat: String) {
    C("C", "C"),
    Cis("C♯", "D♭"),
    D("D", "D"),
    Dis("D♯", "E♭"),
    E("E", "E"),
    F("F", "F"),
    Fis("F♯", "G♭"),
    G("G", "G"),
    Gis("G♯", "A♭"),
    A("A", "A"),
    Ais("A♯", "B♭"),
    B("B", "B");

    fun shift(halfSteps: Int) = shift(this, halfSteps)

    fun getName(display: Display) = when (display) {
        Display.Sharp -> nameSharp
        Display.Flat -> nameFlat
    }

    enum class Display {
        Sharp, Flat
    }

    companion object {
        fun shift(note: Note, halfSteps: Int): Note {
            val notes = values()
            val index = notes.indexOf(note)
            val newIndex = (index + halfSteps) % 12
            return notes[newIndex]
        }

        val CircleOfFifths: List<Note> = listOf(
            C, G, D, A, E, B, Fis, Cis, Gis, Dis, Ais, F
        )
    }
}

