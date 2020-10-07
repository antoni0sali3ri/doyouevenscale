package de.theopensourceguy.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable
import de.theopensourceguy.doyouevenscale.R

data class Interval(
    val halfSteps: Int
) : Parcelable {
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

        fun nameResFor(halfSteps: Int) = NAME_RES[halfSteps % 12]
    }
}

fun Int.toInterval() : Interval {
    return Interval(this)
}