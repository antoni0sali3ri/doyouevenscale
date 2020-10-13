package com.github.antoni0sali3ri.doyouevenscale.core.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.antoni0sali3ri.doyouevenscale.core.db.NoteConverters
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note

/**
 * Fretted string instrument.
 */
@Entity(tableName = "instruments")
data class Instrument(
    var stringCount: Int,
    override var name: String,
) : ListableEntity, Parcelable {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0L

    /**
     * Parcelable implementation.
     */
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    ) {
        id = parcel.readLong()
    }

    init {
        require(stringCount in 0..MaxStrings)
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(stringCount)
        parcel.writeString(name)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Instrument> {
        /**
         * Maximum number of strings an instrument can have.
         *
         * Note: this is based on zero research and might be changed (most likely increased)
         * in the future.
         */
        val MaxStrings = 10

        /**
         * Maximum number of frets an instrument can show.
         *
         * I'm pretty sure this is a sane default.
         */
        val MaxFrets = 30

        override fun createFromParcel(parcel: Parcel): Instrument {
            return Instrument(parcel)
        }

        override fun newArray(size: Int): Array<Instrument?> {
            return arrayOfNulls(size)
        }

    }

    /**
     * Tuning for a fretted instrument.
     *
     * A tuning needs to specify the string pitches for its instrument. As such the number of
     * string pitches needs to be equal to the number of strings the instrument has.
     */
    @Entity(
        tableName = "instrument_tunings",
        foreignKeys = [ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Instrument::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("instrumentId")
        )]
    )
    @TypeConverters(NoteConverters::class)
    data class Tuning(
        var instrumentId: Long,
        var stringPitches: List<Note>,
        override var name: String
    ) : ListableEntity, Parcelable {

        @PrimaryKey(autoGenerate = true)
        override var id: Long = 0

        /**
         * Convenience function for setting string pitches in the order they're usually written.
         *
         * Pitches are stored in order from first string to last string, while the written order
         * is usually the other way around (e.g. guitar standard tuning -> EADGBE is stored as
         * List(E,B,G,D,A,E)). This function takes a list of Notes that corresponds to the written
         * order of a given tuning and saves them accordingly (i.e. reverses the input list).
         */
        fun setPitchesLastToFirst(pitches: List<Note>) {
            stringPitches = pitches.reversed()
        }


        fun pitchOf(stringNo: Int): Note {
            return stringPitches[stringNo - 1]
        }

        /**
         * Parcelable implementation.
         */
        constructor(parcel: Parcel) : this(
            parcel.readLong(),
            emptyList<Note>(),
            parcel.readString()!!
        ) {
            id = parcel.readLong()
            val pitches = mutableListOf<Note>()
            parcel.readTypedList(pitches, Note)
            stringPitches = pitches.toList()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(instrumentId)
            parcel.writeString(name)
            parcel.writeLong(id)
            parcel.writeTypedList(stringPitches)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Tuning> {
            override fun createFromParcel(parcel: Parcel): Tuning {
                return Tuning(parcel)
            }

            override fun newArray(size: Int): Array<Tuning?> {
                return arrayOfNulls(size)
            }
        }
    }
}