package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable
import com.github.antoni0sali3ri.doyouevenscale.core.trueMod

/**
 * The notes of the (western) 12-note system.
 */
enum class Note(val nameSharp: String, val nameFlat: String) : Parcelable {
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

    /**
     * Transpose this note by a number of semitones.
     *
     * Works in both directions, pass a negative integer to transpose this note down.
     *
     * @param semitones number of semitones (half steps) by which to transpose this note.
     *                  Can be negative.
     */
    fun transpose(semitones: Int) = transpose(this, semitones)

    /**
     * Get the name by which to display this note.
     *
     * @param display whether to display this note as sharp or flat
     */
    fun getName(display: Display) = when (display) {
        Display.Sharp -> nameSharp
        Display.Flat -> nameFlat
    }

    /**
     * Defines how to display a note (sharp or flat).
     */
    enum class Display {
        Sharp, Flat
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {

        /**
         * Transpose a given note by a number of semitones.
         *
         * @param note the note to transpose
         * @param semitones number of semitones by which to transpose the note (can be negative)
         */
        fun transpose(note: Note, semitones: Int): Note {
            val notes = values()
            val index = notes.indexOf(note)
            val newIndex = (index + semitones).trueMod(12)
            return notes[newIndex]
        }

        /**
         * The infamous circle of fifths.
         */
        val CircleOfFifths: List<Note> = listOf(
            C, G, D, A, E, B, Fis, Cis, Gis, Dis, Ais, F
        )

        override fun createFromParcel(parcel: Parcel): Note {
            return valueOf(parcel.readString()!!)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}

