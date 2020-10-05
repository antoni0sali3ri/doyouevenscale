package de.theopensourceguy.doyouevenscale.core.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.Scale
import de.theopensourceguy.doyouevenscale.core.model.TunedInstrument

@Dao
interface TuningDao {
    @Query("SELECT * FROM instrument_tunings WHERE tuningId = :id")
    fun getTuningForId(id: Int) : Instrument.Tuning

    @Query("SELECT * FROM instrument_tunings WHERE numStrings = :numStrings")
    fun getTuningsForStringCount(numStrings: Int) : LiveData<List<Instrument.Tuning>>

    @Insert
    fun insertTuning(tuning: Instrument.Tuning) : Long

    @Insert
    fun insertTunings(tunings: List<Instrument.Tuning>)

    @Delete
    fun deleteTuning(tuning: Instrument.Tuning)

    @Delete
    fun deleteTunings(tunings: List<Instrument.Tuning>)
}

@Dao
interface InstrumentDao {
    @Transaction
    @Query("SELECT * FROM instruments INNER JOIN instrument_tunings ON instrumentId = tuningId")
    fun getAllTunedInstruments() : LiveData<List<TunedInstrument>>

    @Transaction
    @Query("SELECT * FROM instruments INNER JOIN instrument_tunings ON tuningId = default_tuning_id WHERE instrumentId = :id")
    fun getTunedInstrumentById(id: Int) : LiveData<TunedInstrument>

    @Query("SELECT * FROM instruments WHERE instrumentId = :id")
    fun getInstrumentById(id: Int) : Instrument

    @Query("SELECT * FROM instruments")
    fun getAllInstruments() : LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments WHERE numStrings = :numStrings")
    fun getInstrumentsWithStringCount(numStrings: Int) : LiveData<List<Instrument>>

    @Insert
    fun insertInstrument(instrument: Instrument) : Long

    @Insert
    fun insertInstruments(instruments: List<Instrument>)

    @Delete
    fun deleteInstrument(instrument: Instrument)
}

@Dao
interface ScaleTypeDao {
    @Query("SELECT * FROM scale_types")
    fun getAllScaleTypes() : LiveData<List<Scale.Type>>

    @Insert
    fun insertScaleType(scaleType: Scale.Type)

    @Insert
    fun insertScaleTypes(scaleTypes: List<Scale.Type>)

    @Delete
    fun deleteScaleType(scaleType: Scale.Type)

    @Delete
    fun deleteScaleTypes(scaleTypes: List<Scale.Type>)


}