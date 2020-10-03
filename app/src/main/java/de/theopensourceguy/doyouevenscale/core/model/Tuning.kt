package de.theopensourceguy.doyouevenscale.core.model

data class Tuning(
    val stringPitches: List<Note>
) {
    /**
     * Create a tuning from its name (strings from low to high).
     *
     * Reverses the order of the notes, so that you can create e.g. standard guitar tuning
     * (EADGBE) like you'd write it down and still have the canonical order of the strings
     * intact (i.e. 1st string = E, 2nd string = B, 3rd string = G, ...)
     */
    constructor(vararg notes: Note) : this(notes.reversed())

    val numStrings = stringPitches.size

    fun pitchOf(stringNo: Int) : Note {
        return stringPitches[stringNo - 1]
    }
}

