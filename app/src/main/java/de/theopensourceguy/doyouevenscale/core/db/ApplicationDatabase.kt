package de.theopensourceguy.doyouevenscale.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.InstrumentPreset
import de.theopensourceguy.doyouevenscale.core.model.ListableEntity
import de.theopensourceguy.doyouevenscale.core.model.Scale

@Database(
    version = 1,
    entities = arrayOf(
        Scale.Type::class,
        Instrument.Tuning::class,
        Instrument::class,
        InstrumentPreset::class
    )
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun instrumentConfigDao(): InstrumentPresetDao

    abstract fun tuningDao(): TuningDao

    abstract fun instrumentDao(): InstrumentDao

    abstract fun scaleDao(): ScaleTypeDao

    fun <T : ListableEntity> getDaoForClass(clazz: Class<T>): ViewModelDao<T> = when(clazz) {
        Instrument::class.java -> instrumentDao()
        InstrumentPreset::class.java -> instrumentConfigDao()
        Instrument.Tuning::class.java -> tuningDao()
        Scale.Type::class.java -> scaleDao()
        else -> throw IllegalArgumentException()
    } as ViewModelDao<T>

}

