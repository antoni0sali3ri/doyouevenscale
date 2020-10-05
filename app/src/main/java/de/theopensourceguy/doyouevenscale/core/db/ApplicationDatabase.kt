package de.theopensourceguy.doyouevenscale.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.Scale
import de.theopensourceguy.doyouevenscale.core.model.TunedInstrument

@Database(version = 1, entities = arrayOf(Scale.Type::class, Instrument.Tuning::class, Instrument::class))
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun tuningDao(): TuningDao

    abstract fun instrumentDao(): InstrumentDao

    abstract fun scaleDao(): ScaleTypeDao

}

class DbUtils(val database: ApplicationDatabase) {
    fun insertInstrumentAndTuning(instrument: TunedInstrument) : Long {
        val id = database.tuningDao().insertTuning(instrument.tuning)
        val resId = database.instrumentDao().insertInstrument(instrument.instrument.apply { defaultTuningId = id.toInt() })
        return resId
    }
}