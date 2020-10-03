package de.theopensourceguy.doyouevenscale.core.model

enum class Note(val nameSharp: String, val nameFlat: String) {
    C("C", "C"),
    Cis("C#", "Db"),
    D("D", "D"),
    Dis("D#", "Eb"),
    E("E", "E"),
    F("F","F"),
    Fis("F#", "Gb"),
    G("G", "G"),
    Gis("G#", "Ab"),
    A("A", "A"),
    Ais("A#", "Bb"),
    B("B", "B");

    fun shift(halfSteps: Int) = shift(this, halfSteps)

    companion object {
        fun shift(note: Note, halfSteps: Int) : Note {
            val notes = values()
            val index = notes.indexOf(note)
            val newIndex = (index + halfSteps) % 12
            return notes[newIndex]
        }
    }
}

