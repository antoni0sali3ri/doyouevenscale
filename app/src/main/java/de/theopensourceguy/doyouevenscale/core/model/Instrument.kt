package de.theopensourceguy.doyouevenscale.core.model

import android.graphics.Point
import androidx.room.*
import de.theopensourceguy.doyouevenscale.core.db.NoteConverters


data class TunedInstrument(
    @Relation(
        parentColumn = "tuningId",
        entityColumn = "default_tuning_id"
    )
    val instrument: Instrument,
    @Embedded val tuning: Instrument.Tuning
) {
    init {
        require(instrument.numStrings == tuning.numStrings)
    }

    fun getNoteAt(stringNo: Int, fretNo: Int): Note {
        require(stringNo > 0 && stringNo <= instrument.numStrings)
        require(fretNo >= 0 && fretNo <= instrument.numFrets)
        return tuning.stringPitches[stringNo - 1].shift(fretNo)
    }

    fun getFretsForScale(scale: Scale): List<Point> = (1..instrument.numStrings).fold(emptyList()) { l, s ->
        l + getFretsForScale(s, scale).map { Point(s, it) }
    }

    fun getFretsForScale(stringNo: Int, scale: Scale): List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return (0..instrument.numFrets).filter {
            scale.contains(openString.shift(it))
        }
    }

    fun getRootsForString(stringNo: Int, root: Note): List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return (0..instrument.numFrets).filter {
            openString.shift(it) == root
        }
    }

    fun getRootsForString(stringNo: Int, scale: Scale): List<Int> =
        getRootsForString(stringNo, scale.root)

    fun getRoots(root: Note): List<Point> =
        (1..instrument.numStrings).fold(emptyList()) { l, s ->
            l + getRootsForString(s, root).map { Point(s, it) }
        }

    fun getRoots(scale: Scale): List<Point> = getRoots(scale.root)
}

@Entity(tableName = "instruments")
data class Instrument(
    var numStrings: Int,
    var numFrets: Int,
    var name: String,
    @ColumnInfo(name = "default_tuning_id")
    var defaultTuningId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var instrumentId: Int = 0

    init {
        require(numFrets > 0 && numFrets <= MaxFrets)
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
        var tuningName: String
    ) {
        init {
            require(stringPitches.isNotEmpty())
        }

        @PrimaryKey(autoGenerate = true)
        var tuningId: Int = 0

        var numStrings: Int = stringPitches.size

        fun pitchOf(stringNo: Int): Note {
            return stringPitches[stringNo - 1]
        }
    }
}

