package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.os.Parcel
import android.os.Parcelable

class ObservableInstrumentConfiguration(
    _instrument: Instrument,
    _tuning: Instrument.Tuning,
    _root: Note,
    _scaleType: Scale.Type,
    _fretsShown: IntRange,
    _noteDisplay: Note.Display
) : Parcelable {
    val listeners: MutableSet<OnChangeListener> = mutableSetOf()

    val instrument: Instrument = _instrument

    var tuning: Instrument.Tuning = _tuning
        set(value) {
            listeners.forEach { it.onTuningChanged(value, field) }
            field = value
        }

    var rootNote: Note = _root
        set(value) {
            listeners.forEach {
                it.onScaleChanged(
                    Scale(value, scaleType),
                    Scale(field, scaleType)
                )
            }
            field = value
        }

    var scaleType: Scale.Type = _scaleType
        set(value) {
            listeners.forEach { it.onScaleChanged(Scale(rootNote, value), Scale(rootNote, field)) }
            field = value
        }

    var fretsShown: IntRange = _fretsShown
        set(value) {
            listeners.forEach { it.onFretRangeChanged(value, field) }
            field = value
        }

    var noteDisplay: Note.Display = _noteDisplay
        set(value) {
            listeners.forEach { it.onNoteDisplayChanged(value, field) }
            field = value
        }

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Instrument::class.java.classLoader)!!,
        parcel.readParcelable(Instrument.Tuning::class.java.classLoader)!!,
        parcel.readParcelable(Note::class.java.classLoader)!!,
        parcel.readParcelable(Scale.Type::class.java.classLoader)!!,
        parcel.readInt()..parcel.readInt(),
        Note.Display.valueOf(parcel.readString()!!)
    )

    fun getObjectForDb(configId: Long = 0): InstrumentPreset {
        return InstrumentPreset(
            instrument.id,
            tuning.id,
            scaleType.id,
            rootNote,
            fretsShown.first,
            fretsShown.last
        ).apply { id = configId }
    }

    interface OnChangeListener {
        fun onTuningChanged(newTuning: Instrument.Tuning, oldTuning: Instrument.Tuning)

        fun onScaleChanged(newScale: Scale, oldScale: Scale)

        fun onFretRangeChanged(newRange: IntRange, oldRange: IntRange)

        fun onNoteDisplayChanged(newDisplay: Note.Display, oldDisplay: Note.Display)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeParcelable(instrument, flags)
            writeParcelable(tuning, flags)
            writeParcelable(rootNote, flags)
            writeParcelable(scaleType, flags)
            writeInt(fretsShown.first)
            writeInt(fretsShown.last)
            writeString(noteDisplay.toString())
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ObservableInstrumentConfiguration> {
        override fun createFromParcel(parcel: Parcel): ObservableInstrumentConfiguration {
            return ObservableInstrumentConfiguration(parcel)
        }

        override fun newArray(size: Int): Array<ObservableInstrumentConfiguration?> {
            return arrayOfNulls(size)
        }
    }
}

