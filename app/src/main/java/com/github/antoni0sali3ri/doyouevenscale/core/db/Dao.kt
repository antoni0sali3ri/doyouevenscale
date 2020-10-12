package com.github.antoni0sali3ri.doyouevenscale.core.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale

interface ViewModelDao<T : ListableEntity> {

    fun getAll(): LiveData<out List<T>>

    fun getSingle(id: Long): T

    @Insert
    suspend fun insertSingle(item: T): Long

    @Insert
    suspend fun insertMultiple(items: List<T>)

    @Update
    suspend fun updateSingle(item: T)

    @Update
    suspend fun updateMultiple(items: List<T>): Int

    suspend fun deleteSingleById(id: Long)

    @Delete
    suspend fun deleteSingle(item: T)

    @Delete
    suspend fun deleteMultiple(items: List<T>)
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

    @Query("DELETE FROM instrument_configurations WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)
}

@Dao
interface TuningDao : ViewModelDao<Instrument.Tuning> {
    @Query("""SELECT instrument_tunings.id,instrumentId,stringPitches,instrument_tunings.name 
                     FROM instrument_tunings 
                     INNER JOIN instruments 
                     ON instrument_tunings.instrumentId = instruments.id 
                     ORDER BY instruments.name,instrument_tunings.name ASC
                     """)
    override fun getAll(): LiveData<List<Instrument.Tuning>>

    @Query("SELECT * FROM instrument_tunings WHERE id = :id")
    override fun getSingle(id: Long): Instrument.Tuning

    @Query("SELECT * FROM instrument_tunings WHERE instrumentId = :instrumentId ORDER BY name ASC")
    fun getTuningsForInstrument(instrumentId: Long): LiveData<List<Instrument.Tuning>>

    @Query("DELETE FROM instrument_tunings WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)
}

@Dao
interface InstrumentDao : ViewModelDao<Instrument> {
    @Query("SELECT * FROM instruments WHERE id = :id")
    override fun getSingle(id: Long): Instrument

    @Query("SELECT * FROM instruments WHERE id IN(:ids)")
    fun getInstrumentsByIds(ids: List<Long>): LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments ORDER BY stringCount,name ASC")
    override fun getAll(): LiveData<List<Instrument>>

    @Query("SELECT * FROM instruments WHERE stringCount = :stringCount")
    fun getInstrumentsWithStringCount(stringCount: Int): LiveData<List<Instrument>>

    @Query("DELETE FROM instruments WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)
}

@Dao
interface ScaleTypeDao : ViewModelDao<Scale.Type> {
    @Query("SELECT * FROM scale_types")
    override fun getAll(): LiveData<List<Scale.Type>>

    @Query("SELECT * FROM scale_types WHERE id = :id")
    override fun getSingle(id: Long): Scale.Type

    @Query("DELETE FROM scale_types WHERE id = :id")
    override suspend fun deleteSingleById(id: Long)
}