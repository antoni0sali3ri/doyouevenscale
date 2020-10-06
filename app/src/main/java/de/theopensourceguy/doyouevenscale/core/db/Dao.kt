package de.theopensourceguy.doyouevenscale.core.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.InstrumentConfiguration
import de.theopensourceguy.doyouevenscale.core.model.ListableEntity
import de.theopensourceguy.doyouevenscale.core.model.Scale

interface ViewModelDao<T : ListableEntity> {

    fun getAll(): LiveData<out List<T>>

    fun insertSingle(item: T): Long

    fun updateSingle(item: T)

    fun deleteSingle(item: T)
}

@Dao
interface InstrumentConfigurationDao : ViewModelDao<InstrumentConfiguration> {

    @Query("SELECT * FROM instrument_configurations")
    override fun getAll(): LiveData<List<InstrumentConfiguration>>

    @Query("SELECT * FROM instrument_configurations WHERE id = :id")
    fun getInstrumentConfigById(id: Long): InstrumentConfiguration

    @Query("SELECT * FROM instrument_configurations WHERE id IN(:ids)")
    fun getInstrumentConfigsByIds(ids: List<Long>): LiveData<List<InstrumentConfiguration>>

    @Insert
    fun insertInstrumentConfigs(instrumentConfigurations: List<InstrumentConfiguration>): List<Long>

    @Insert
    override fun insertSingle(item: InstrumentConfiguration): Long

    @Update
    override fun updateSingle(item: InstrumentConfiguration)

    @Delete
    override fun deleteSingle(item: InstrumentConfiguration)
}

@Dao
interface TuningDao : ViewModelDao<Instrument.Tuning> {
    @Query("SELECT * FROM instrument_tunings")
    override fun getAll(): LiveData<List<Instrument.Tuning>>

    @Query("SELECT * FROM instrument_tunings WHERE id = :id")
    fun getTuningById(id: Long): Instrument.Tuning

    @Query("SELECT * FROM instrument_tunings WHERE numStrings = :numStrings")
    fun getTuningsByStringCount(numStrings: Int): LiveData<List<Instrument.Tuning>>

    @Insert
    override fun insertSingle(item: Instrument.Tuning): Long

    @Insert
    fun insertTunings(tunings: List<Instrument.Tuning>)

    @Update
    override fun updateSingle(item: Instrument.Tuning)

    @Delete
    override fun deleteSingle(item: Instrument.Tuning)

    @Delete
    fun deleteTunings(tunings: List<Instrument.Tuning>)
}

@Dao
interface InstrumentDao : ViewModelDao<Instrument> {
    @Query("SELECT * FROM instruments WHERE id = :id")
    fun getInstrumentById(id: Long): Instrument

    @Query("SELECT * FROM instruments WHERE id IN(:ids)")
    fun getInstrumentsByIds(ids: List<Long>): LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments")
    override fun getAll(): LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments WHERE numStrings = :numStrings")
    fun getInstrumentsWithStringCount(numStrings: Int): LiveData<List<Instrument>>

    @Insert
    override fun insertSingle(item: Instrument): Long

    @Insert
    fun insertInstruments(instruments: List<Instrument>)

    @Update
    override fun updateSingle(item: Instrument)

    @Delete
    override fun deleteSingle(item: Instrument)
}

@Dao
interface ScaleTypeDao : ViewModelDao<Scale.Type> {
    @Query("SELECT * FROM scale_types")
    override fun getAll(): LiveData<List<Scale.Type>>

    @Query("SELECT * FROM scale_types WHERE id = :id")
    fun getScaleTypeById(id: Long): Scale.Type

    @Insert
    override fun insertSingle(item: Scale.Type): Long

    @Insert
    fun insertScaleTypes(scaleTypes: List<Scale.Type>): List<Long>

    @Update
    override fun updateSingle(item: Scale.Type)

    @Delete
    override fun deleteSingle(item: Scale.Type)

    @Delete
    fun deleteScaleTypes(scaleTypes: List<Scale.Type>)
}