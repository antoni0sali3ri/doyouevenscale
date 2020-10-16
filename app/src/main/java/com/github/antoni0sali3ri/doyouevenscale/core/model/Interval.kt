package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable
import com.github.antoni0sali3ri.doyouevenscale.R

class Interval private constructor(val semitones: Int) : Parcelable, Comparable<Interval> {

    init {
        require(semitones >= 0)
    }

    /**
     * String resource for the name of this interval.
     */
    val nameRes = nameResFor(semitones)

    constructor(parcel: Parcel) : this(parcel.readInt())

    fun transpose(note: Note, transposeUp: Boolean = true): Note =
        note.transpose(if (transposeUp) semitones else -semitones)

    override fun toString(): String = "Interval($semitones)"

    override fun compareTo(other: Interval): Int = semitones.compareTo(other.semitones)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(semitones)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Interval> {
        val NAME_RES = arrayOf(
            R.string.interval_prime,
            R.string.interval_minor_second,
            R.string.interval_major_second,
            R.string.interval_minor_third,
            R.string.interval_major_third,
            R.string.interval_fourth,
            R.string.interval_tritone,
            R.string.interval_fifth,
            R.string.interval_minor_sixth,
            R.string.interval_major_sixth,
            R.string.interval_minor_seventh,
            R.string.interval_major_seventh
        )

        val INTERVALS = (0..11).map { Interval(it) }.toTypedArray()

        fun nameResFor(halfSteps: Int) = NAME_RES[halfSteps % 12]

        operator fun invoke(semitones: Int) = INTERVALS[semitones % 12]

        override fun createFromParcel(parcel: Parcel): Interval {
            return Interval(parcel)
        }

        override fun newArray(size: Int): Array<Interval?> {
            return arrayOfNulls(size)
        }
    }
}

fun Int.semitones(): Interval {
    return Interval(this)
}