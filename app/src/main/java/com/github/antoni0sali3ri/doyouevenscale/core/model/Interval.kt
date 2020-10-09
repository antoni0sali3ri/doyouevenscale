package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable
import com.github.antoni0sali3ri.doyouevenscale.R

data class Interval(
    val halfSteps: Int
) : Parcelable, Comparable<Interval> {
    init {
        require(halfSteps >= 0)
    }
    val nameRes = nameResFor(halfSteps)

    constructor(parcel: Parcel) : this(parcel.readInt())

    fun shift(note: Note, shiftUp: Boolean = true) : Note = note.shift(if (shiftUp) halfSteps else -halfSteps)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(halfSteps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Interval> {
        override fun createFromParcel(parcel: Parcel): Interval {
            return Interval(parcel)
        }

        override fun newArray(size: Int): Array<Interval?> {
            return arrayOfNulls(size)
        }


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
    }

    override fun compareTo(other: Interval): Int = halfSteps.compareTo(other.halfSteps)
}

fun Int.toInterval() : Interval {
    return Interval(this)
}