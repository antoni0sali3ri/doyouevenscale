package com.github.antoni0sali3ri.doyouevenscale.core.db

import androidx.room.TypeConverter
import com.github.antoni0sali3ri.doyouevenscale.core.model.Interval
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note
import com.github.antoni0sali3ri.doyouevenscale.core.model.semitones

const val LIST_SEP = ";;"

class NoteConverters {
    @TypeConverter
    fun noteToString(note: Note) = note.toString()

    @TypeConverter
    fun stringToNote(string: String) = Note.valueOf(string)

    @TypeConverter
    fun noteListToString(notes: List<Note>) = notes.joinToString(LIST_SEP)

    @TypeConverter
    fun stringToNoteList(string: String) = string.split(LIST_SEP).map { Note.valueOf(it) }
}

class IntervalConverters {
    @TypeConverter
    fun intervalListToString(intervals: List<Interval>) =
        intervals
            .map { it.semitones }
            .joinToString(LIST_SEP)

    @TypeConverter
    fun stringToIntervalList(string: String) = string
        .split(LIST_SEP)
        .map { it.toInt().semitones() }
}

