package de.theopensourceguy.doyouevenscale.core.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.InstrumentPreset
import de.theopensourceguy.doyouevenscale.core.model.ListableEntity
import de.theopensourceguy.doyouevenscale.core.model.Scale

interface ViewModelDao<T : ListableEntity> {

    fun getAll(): LiveData<out List<T>>

    fun getSingle(id: Long) : T

    suspend fun insertSingle(item: T): Long

    suspend fun updateSingle(item: T)

    suspend fun deleteSingleById(id: Long)

    suspend fun deleteSingle(item: T)
}

@Dao
interface InstrumentPresetDao : ViewModelDao<InstrumentPreset> {

    @Query("SELECT * FROM instrument_configurations")
    override fun getAll(): LiveData<List<InstrumentPreset>>

    @Query("SELECT * FROM instrument_configurations WHERE id = :id")
    override fun getSingle(id: Long): InstrumentPreset

    @Query("SELECT * FROM instrument_configurations WHERE showAsTab >= 0")
    fun getInstrumentPresetTabs() : LiveData<List<InstrumentPreset>>

    @Query("SELECT * FROM instrument_configurations WHERE id IN(:ids)")
    fun getInstrumentPresetsByIds(ids: List<Long>): LiveData<List<InstrumentPreset>>

    @Insert
    fun insertInstrumentPresets(instrumentPresets: List<InstrumentPreset>): List<Long>

    @Insert
    override suspend fun insertSingle(item: InstrumentPreset): Long

    @Update
    override suspend fun updateSingle(item: InstrumentPreset)

    @Query("DELETE FROM instrument_configurations WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)

    @Delete
    override suspend fun deleteSingle(item: InstrumentPreset)
}

@Dao
interface TuningDao : ViewModelDao<Instrument.Tuning> {
    @Query("SELECT * FROM instrument_tunings ORDER BY numStrings,name ASC")
    override fun getAll(): LiveData<List<Instrument.Tuning>>

    @Query("SELECT * FROM instrument_tunings WHERE id = :id")
    override fun getSingle(id: Long): Instrument.Tuning

    @Query("SELECT * FROM instrument_tunings WHERE numStrings = :numStrings ORDER BY name ASC")
    fun getTuningsByStringCount(numStrings: Int): LiveData<List<Instrument.Tuning>>

    @Insert
    override suspend fun insertSingle(item: Instrument.Tuning): Long

    @Insert
    fun insertTunings(tunings: List<Instrument.Tuning>)

    @Update
    override suspend fun updateSingle(item: Instrument.Tuning)

    @Query("DELETE FROM instrument_tunings WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)

    @Delete
    override suspend fun deleteSingle(item: Instrument.Tuning)

    @Delete
    fun deleteTunings(tunings: List<Instrument.Tuning>)
}

@Dao
interface InstrumentDao : ViewModelDao<Instrument> {
    @Query("SELECT * FROM instruments WHERE id = :id")
    override fun getSingle(id: Long): Instrument

    @Query("SELECT * FROM instruments WHERE id IN(:ids)")
    fun getInstrumentsByIds(ids: List<Long>): LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments ORDER BY numStrings,name ASC")
    override fun getAll(): LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments WHERE numStrings = :numStrings")
    fun getInstrumentsWithStringCount(numStrings: Int): LiveData<List<Instrument>>

    @Insert
    override suspend fun insertSingle(item: Instrument): Long

    @Insert
    fun insertInstruments(instruments: List<Instrument>)

    @Update
    override suspend fun updateSingle(item: Instrument)

    @Query("DELETE FROM instruments WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)

    @Delete
    override suspend fun deleteSingle(item: Instrument)
}

@Dao
interface ScaleTypeDao : ViewModelDao<Scale.Type> {
    @Query("SELECT * FROM scale_types")
    override fun getAll(): LiveData<List<Scale.Type>>

    @Query("SELECT * FROM scale_types WHERE id = :id")
    override fun getSingle(id: Long): Scale.Type

    @Insert
    override suspend fun insertSingle(item: Scale.Type): Long

    @Insert
    fun insertScaleTypes(scaleTypes: List<Scale.Type>): List<Long>

    @Update
    override suspend fun updateSingle(item: Scale.Type)

    @Query("DELETE FROM scale_types WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)

    @Delete
    override suspend fun deleteSingle(item: Scale.Type)

    @Delete
    fun deleteScaleTypes(scaleTypes: List<Scale.Type>)
}