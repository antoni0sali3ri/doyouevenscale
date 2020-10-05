package de.theopensourceguy.doyouevenscale.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.InstrumentConfiguration
import de.theopensourceguy.doyouevenscale.core.model.Scale
import de.theopensourceguy.doyouevenscale.core.model.TunedInstrument

@Database(
    version = 1,
    entities = arrayOf(
        Scale.Type::class,
        Instrument.Tuning::class,
        Instrument::class,
        InstrumentConfiguration::class
    )
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun instrumentConfigDao(): InstrumentConfigurationDao

    abstract fun tuningDao(): TuningDao

    abstract fun instrumentDao(): InstrumentDao

    abstract fun scaleDao(): ScaleTypeDao

}

class DbUtils(val database: ApplicationDatabase) {

    fun insertInstrumentConfiguration(
        instrument: TunedInstrument,
        scale: Scale,
        fretsShown: IntRange
    ): Long {
        val tuningId = database.tuningDao().insertTuning(instrument.tuning)
        val instrumentId = database.instrumentDao().insertInstrument(instrument.instrument)
        val rootNote = scale.root
        val scaleTypeId = database.scaleDao().insertScaleType(scale.type)
        val instrumentConfig = InstrumentConfiguration(
            instrumentId,
            tuningId,
            scaleTypeId,
            rootNote,
            fretsShown.first,
            fretsShown.last
        )
        return database.instrumentConfigDao().insertInstrumentConfig(instrumentConfig)
    }
}