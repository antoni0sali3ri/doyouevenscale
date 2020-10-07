package de.theopensourceguy.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable
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
        override var name: String
    ) : ListableEntity, Parcelable {
        @PrimaryKey(autoGenerate = true)
        override var id: Long = 0

        constructor(parcel: Parcel) : this(
            emptyList(),
            parcel.readString()!!
        ) {
            id = parcel.readLong()
        }

        constructor(
            name: String,
            vararg intervalsNum: Int,
        ) : this(intervalsNum.toTypedArray().map { it.toInterval() }, name)

        fun notesInKey(root: Note): List<Note> {
            return listOf(root) + intervals.map { it.shift(root) }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeLong(id)
            parcel.readTypedList(intervals, Interval.CREATOR)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Type> {
            override fun createFromParcel(parcel: Parcel): Type {
                return Type(parcel)
            }

            override fun newArray(size: Int): Array<Type?> {
                return arrayOfNulls(size)
            }
        }

    }
}

