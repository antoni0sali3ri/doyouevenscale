package de.theopensourceguy.doyouevenscale.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.theopensourceguy.doyouevenscale.core.db.IntervalConverters

data class Scale(
    val root: Note,
    val type: Type
) {
    val notes = type.notesInKey(root)

    val name = "$root ${type.name}"

    fun contains(note: Note) = notes.contains(note)

    @Entity(tableName = "scale_types")
    @TypeConverters(IntervalConverters::class)
    data class Type(
        var intervals: List<Interval>,
        var name: String
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0

        constructor(
            name: String,
            vararg intervalsNum: Int,
        ) : this(intervalsNum.toTypedArray().map { it.toInterval() }, name)

        fun notesInKey(root: Note): List<Note> {
            return listOf(root) + intervals.map { it.shift(root) }
        }

    }
}

