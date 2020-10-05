package de.theopensourceguy.doyouevenscale.core.db

import androidx.room.TypeConverter
import de.theopensourceguy.doyouevenscale.core.model.Interval
import de.theopensourceguy.doyouevenscale.core.model.Note
import de.theopensourceguy.doyouevenscale.core.model.toInterval

const val LIST_SEP = ";;"

class NoteConverters {
    @TypeConverter
    fun noteListToString(notes: List<Note>) = notes.joinToString(LIST_SEP)

    @TypeConverter
    fun stringToNoteList(string: String) = string.split(LIST_SEP).map { Note.valueOf(it) }
}

class IntervalConverters {
    @TypeConverter
    fun intervalListToString(intervals: List<Interval>) =
        intervals
            .map { it.halfSteps }
            .joinToString(LIST_SEP)

    @TypeConverter
    fun stringToIntervalList(string: String) = string
        .split(LIST_SEP)
        .map { it.toInt().toInterval() }
}

