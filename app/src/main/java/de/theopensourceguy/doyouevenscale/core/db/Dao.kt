package de.theopensourceguy.doyouevenscale.core.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.InstrumentConfiguration
import de.theopensourceguy.doyouevenscale.core.model.Scale

@Dao
interface InstrumentConfigurationDao {
    @Query("SELECT * FROM instrument_configurations")
    fun getAllInstrumentConfigs() : LiveData<List<InstrumentConfiguration>>

    @Query("SELECT * FROM instrument_configurations WHERE id = :id")
    fun getInstrumentConfigById(id: Long) : InstrumentConfiguration

    @Query("SELECT * FROM instrument_configurations WHERE id IN(:ids)")
    fun getInstrumentConfigsByIds(ids: List<Long>) : LiveData<List<InstrumentConfiguration>>

    @Insert
    fun insertInstrumentConfigs(instrumentConfigurations: List<InstrumentConfiguration>): List<Long>

    @Insert
    fun insertInstrumentConfig(instrumentConfiguration: InstrumentConfiguration) : Long

    @Update
    fun updateInstrumentConfig(instrumentConfiguration: InstrumentConfiguration)

    @Delete
    fun deleteInstrumentConfig(instrumentConfiguration: InstrumentConfiguration)
}

@Dao
interface TuningDao {
    @Query("SELECT * FROM instrument_tunings WHERE tuningId = :id")
    fun getTuningById(id: Long) : Instrument.Tuning

    @Query("SELECT * FROM instrument_tunings WHERE numStrings = :numStrings")
    fun getTuningsByStringCount(numStrings: Int) : LiveData<List<Instrument.Tuning>>

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
    @Query("SELECT * FROM instruments WHERE instrumentId = :id")
    fun getInstrumentById(id: Long) : Instrument

    @Query("SELECT * FROM instruments WHERE instrumentId IN(:ids)")
    fun getInstrumentsByIds(ids: List<Long>): LiveData<List<Instrument>>

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

    @Query("SELECT * FROM scale_types WHERE id = :id")
    fun getScaleTypeById(id: Long) : Scale.Type

    @Insert
    fun insertScaleType(scaleType: Scale.Type) : Long

    @Insert
    fun insertScaleTypes(scaleTypes: List<Scale.Type>) : List<Long>

    @Delete
    fun deleteScaleType(scaleType: Scale.Type)

    @Delete
    fun deleteScaleTypes(scaleTypes: List<Scale.Type>)


}