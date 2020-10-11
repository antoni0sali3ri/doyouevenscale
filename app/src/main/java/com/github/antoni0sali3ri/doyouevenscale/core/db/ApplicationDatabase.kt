package com.github.antoni0sali3ri.doyouevenscale.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.antoni0sali3ri.doyouevenscale.core.model.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.Scale

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

    abstract fun instrumentPresetDao(): InstrumentPresetDao

    abstract fun tuningDao(): TuningDao

    abstract fun instrumentDao(): InstrumentDao

    abstract fun scaleDao(): ScaleTypeDao

    fun <T : ListableEntity> getDaoForClass(clazz: Class<T>): ViewModelDao<T> = when(clazz) {
        Instrument::class.java -> instrumentDao()
        InstrumentPreset::class.java -> instrumentPresetDao()
        Instrument.Tuning::class.java -> tuningDao()
        Scale.Type::class.java -> scaleDao()
        else -> throw IllegalArgumentException()
    } as ViewModelDao<T>

}

