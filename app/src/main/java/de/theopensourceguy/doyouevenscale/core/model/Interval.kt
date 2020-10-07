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
            R.string.intervalPrime,
            R.string.intervalMinSecond,
            R.string.intervalMajSecond,
            R.string.intervalMinThird,
            R.string.intervalMajThird,
            R.string.intervalFourth,
            R.string.intervalTritone,
            R.string.intervalFifth,
            R.string.intervalMinSixth,
            R.string.intervalMajSixth,
            R.string.intervalMinSeventh,
            R.string.intervalMajSeventh
        )

        fun nameResFor(halfSteps: Int) = NAME_RES[halfSteps % 12]
    }
}

fun Int.toInterval() : Interval {
    return Interval(this)
}