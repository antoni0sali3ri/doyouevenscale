package de.theopensourceguy.doyouevenscale.core.model

import android.graphics.Point
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.theopensourceguy.doyouevenscale.core.db.NoteConverters

@Entity(
    tableName = "instrument_configurations",
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = Instrument::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("instrumentId")
    ), ForeignKey(
        onDelete = CASCADE,
        entity = Instrument.Tuning::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tuningId")
    ), ForeignKey(
        onDelete = CASCADE,
        entity = Scale.Type::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("scaleTypeId")
    )]
)
@TypeConverters(NoteConverters::class)
data class InstrumentPreset(
    var instrumentId: Long,
    var tuningId: Long,
    var scaleTypeId: Long,
    var rootNote: Note,
    var fromFret: Int,
    var toFret: Int
) : ListableEntity {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0L

    override var name: String = ""

    var showAsTab: Int = -1
}

data class TunedInstrument(
    val instrument: Instrument,
    val tuning: Instrument.Tuning
) {
    init {
        require(instrument.numStrings == tuning.numStrings)
    }

    fun getNoteAt(stringNo: Int, fretNo: Int): Note {
        require(stringNo > 0 && stringNo <= instrument.numStrings)
        require(fretNo >= 0 && fretNo <= Instrument.MaxFrets)
        return tuning.stringPitches[stringNo - 1].shift(fretNo)
    }

    fun getFretsForScale(scale: Scale, fretsShown: IntRange = 0..Instrument.MaxFrets): List<Point> =
        (1..instrument.numStrings).fold(emptyList()) { l, s ->
            l + getFretsForScale(s, scale, fretsShown).map { Point(s, it - fretsShown.first) }
        }

    fun getFretsForScale(
        stringNo: Int,
        scale: Scale,
        fretsShown: IntRange = 0..Instrument.MaxFrets
    ): List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return fretsShown.filter {
            scale.contains(openString.shift(it))
        }
    }

    fun getRootsForString(
        stringNo: Int,
        root: Note,
        fretsShown: IntRange = 0..Instrument.MaxFrets
    ): List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return fretsShown.filter {
            openString.shift(it) == root
        }
    }

    fun getRootsForString(
        stringNo: Int,
        scale: Scale,
        fretsShown: IntRange = 0..Instrument.MaxFrets
    ): List<Int> =
        getRootsForString(stringNo, scale.root, fretsShown)

    fun getRoots(root: Note, fretsShown: IntRange = 0..Instrument.MaxFrets): List<Point> =
        (1..instrument.numStrings).fold(emptyList()) { l, s ->
            l + getRootsForString(s, root, fretsShown).map { Point(s, it - fretsShown.first) }
        }

    fun getRoots(scale: Scale, fretsShown: IntRange = 0..Instrument.MaxFrets): List<Point> =
        getRoots(scale.root, fretsShown)
}

@Entity(tableName = "instruments")
data class Instrument(
    var numStrings: Int,
    override var name: String,
) : ListableEntity, Parcelable {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    ) {
        id = parcel.readLong()
    }

    init {
        require(numStrings >= 0 && numStrings <= MaxStrings)
    }

    @Entity(tableName = "instrument_tunings")
    @TypeConverters(NoteConverters::class)
    data class Tuning(
        var stringPitches: List<Note>,
        override var name: String
    ) : ListableEntity, Parcelable {

        @PrimaryKey(autoGenerate = true)
        override var id: Long = 0

        var numStrings: Int = stringPitches.size

        fun setPitches(pitches: List<Note>) {
            stringPitches = pitches
            numStrings = pitches.size
        }

        constructor(parcel: Parcel) : this(
            emptyList<Note>(),
            parcel.readString()!!
        ) {
            id = parcel.readLong()
            numStrings = parcel.readInt()
            val pitches = mutableListOf<Note>()
            parcel.readTypedList(pitches, Note.CREATOR)
            stringPitches = pitches.toList()
        }

        fun pitchOf(stringNo: Int): Note {
            return stringPitches[stringNo - 1]
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeLong(id)
            parcel.writeInt(numStrings)
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(numStrings)
        parcel.writeString(name)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Instrument> {
        override fun createFromParcel(parcel: Parcel): Instrument {
            return Instrument(parcel)
        }

        override fun newArray(size: Int): Array<Instrument?> {
            return arrayOfNulls(size)
        }

        val MaxStrings = 10
        val MaxFrets = 30 // Any more than that and it's gonna look messy af
    }
}

