package com.github.antoni0sali3ri.doyouevenscale.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.ScaleViewerApplication
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale

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

    @SuppressWarnings("unchecked")
    fun <T : ListableEntity> getDaoForClass(clazz: Class<T>): ViewModelDao<T> = when (clazz) {
        Instrument::class.java -> instrumentDao()
        InstrumentPreset::class.java -> instrumentPresetDao()
        Instrument.Tuning::class.java -> tuningDao()
        Scale.Type::class.java -> scaleDao()
        else -> throw IllegalArgumentException()
    } as ViewModelDao<T>

    companion object {
        private var instance: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            if (instance == null)
                initializeDatabase(context)

            return instance
                ?: throw NullPointerException("Database is null! It really shouldn't be.")
        }

        private fun initializeDatabase(context: Context) {
            instance = if (ScaleViewerApplication.isDebug) {
                Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java)
                    //.allowMainThreadQueries()
                    .build()
            } else {
                Room
                    .databaseBuilder(
                        context,
                        ApplicationDatabase::class.java,
                        "${BuildConfig.APPLICATION_ID}.db"
                    )
                    .build()
            }
        }
    }
}

