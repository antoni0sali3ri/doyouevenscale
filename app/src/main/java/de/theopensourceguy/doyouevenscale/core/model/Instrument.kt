package de.theopensourceguy.doyouevenscale.core.model

import android.graphics.Point
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.theopensourceguy.doyouevenscale.core.db.NoteConverters

@Entity(
    tableName = "instrument_configurations",
    foreignKeys = [ForeignKey(
        entity = Instrument::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("instrumentId")
    ), ForeignKey(
        entity = Instrument.Tuning::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tuningId")
    ), ForeignKey(
        entity = Scale.Type::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("scaleTypeId")
    )]
)
@TypeConverters(NoteConverters::class)
data class InstrumentConfiguration(

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
) : ListableEntity {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0

    init {
        require(numStrings >= 0 && numStrings <= MaxStrings)
    }

    companion object {
        val MaxStrings = 10
        val MaxFrets = 30 // Any more than that and it's gonna look messy af
    }

    @Entity(tableName = "instrument_tunings")
    @TypeConverters(NoteConverters::class)
    data class Tuning(
        var stringPitches: List<Note>,
        override var name: String
    ) : ListableEntity {
        init {
            require(stringPitches.isNotEmpty())
        }

        @PrimaryKey(autoGenerate = true)
        override var id: Long = 0

        var numStrings: Int = stringPitches.size

        fun pitchOf(stringNo: Int): Note {
            return stringPitches[stringNo - 1]
        }
    }
}

