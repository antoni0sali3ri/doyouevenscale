package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable

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

    fun shift(halfSteps: Int) = shift(this, halfSteps)

    fun getName(display: Display) = when (display) {
        Display.Sharp -> nameSharp
        Display.Flat -> nameFlat
    }

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
        override fun createFromParcel(parcel: Parcel): Note {
            return valueOf(parcel.readString()!!)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }

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

