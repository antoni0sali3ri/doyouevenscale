package de.theopensourceguy.doyouevenscale.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.theopensourceguy.doyouevenscale.core.model.*

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

    fun <T : ListableEntity> getDaoForClass(clazz: Class<T>): ViewModelDao<T> = when(clazz) {
        Instrument::class.java -> instrumentDao()
        InstrumentConfiguration::class.java -> instrumentConfigDao()
        Instrument.Tuning::class.java -> tuningDao()
        Scale.Type::class.java -> scaleDao()
        else -> throw IllegalArgumentException()
    } as ViewModelDao<T>

}

class DbUtils(val database: ApplicationDatabase) {

    fun insertInstrumentConfiguration(
        instrument: TunedInstrument,
        scale: Scale,
        fretsShown: IntRange
    ): Long {
        val tuningId = database.tuningDao().insertSingle(instrument.tuning)
        val instrumentId = database.instrumentDao().insertSingle(instrument.instrument)
        val rootNote = scale.root
        val scaleTypeId = database.scaleDao().insertSingle(scale.type)
        val instrumentConfig = InstrumentConfiguration(
            instrumentId,
            tuningId,
            scaleTypeId,
            rootNote,
            fretsShown.first,
            fretsShown.last
        )
        return database.instrumentConfigDao().insertSingle(instrumentConfig)
    }
}