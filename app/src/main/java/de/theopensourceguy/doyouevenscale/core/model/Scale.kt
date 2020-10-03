package de.theopensourceguy.doyouevenscale.core.model

data class Scale(
    val root: Note,
    val type: ScaleType
) {
    val notes = type.notesInKey(root)

    fun contains(note: Note) = notes.contains(note)
}

class ScaleType(val intervals: List<Interval>) {
    constructor(vararg intervalsNum: Int) : this(intervalsNum.map { Interval(it) })

    fun notesInKey(root: Note) : List<Note> {
        return listOf(root) + intervals.map { it.shift(root) }
    }
}

