package com.github.antoni0sali3ri.doyouevenscale.core.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.antoni0sali3ri.doyouevenscale.core.db.NoteConverters
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note

/**
 * A preset for display in MainActivity.
 */
@Entity(
    tableName = "instrument_configurations",
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = Instrument::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("instrumentId")
    ), ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = Instrument.Tuning::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tuningId")
    ), ForeignKey(
        onDelete = ForeignKey.CASCADE,
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

    /**
     * Whether or not to show this preset as a tab.
     *
     * Negative values hide this preset from the tab list in MainActivity, positive values define
     * the position to display this tab.
     */
    var showAsTab: Int = -1
}