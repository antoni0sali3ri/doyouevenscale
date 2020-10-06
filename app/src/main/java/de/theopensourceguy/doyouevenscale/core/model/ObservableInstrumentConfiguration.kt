package de.theopensourceguy.doyouevenscale.core.model

class ObservableInstrumentConfiguration(
    _instrument: Instrument,
    _tuning: Instrument.Tuning,
    _root: Note,
    _scaleType: Scale.Type,
    _fretsShown: IntRange,
    _noteDisplay: Note.Display
) {
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

    fun getObjectForDb(configId: Long = 0): InstrumentConfiguration {
        return InstrumentConfiguration(
            instrument.instrumentId,
            tuning.tuningId,
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
}

