package com.github.antoni0sali3ri.doyouevenscale.core.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.antoni0sali3ri.doyouevenscale.MyApp
import com.github.antoni0sali3ri.doyouevenscale.core.db.ViewModelDao
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import kotlinx.coroutines.launch

sealed class EntityViewModel<T : ListableEntity>(application: Application, clazz: Class<T>) : AndroidViewModel(application) {
    private val dao: ViewModelDao<T> = MyApp.getDatabase(application).getDaoForClass(clazz)

    val items: LiveData<out List<T>> = dao.getAll()

    fun getSingle(id: Long) = dao.getSingle(id)

    fun insert(item: T) = viewModelScope.launch {
        dao.insertSingle(item)
    }

    fun update(item: T) = viewModelScope.launch {
        dao.updateSingle(item)
    }

    fun delete(item: T) = viewModelScope.launch {
        dao.deleteSingle(item)
    }

    fun delete(id: Long) = viewModelScope.launch {
        dao.deleteSingleById(id)
    }
}

class TuningViewModel(application: Application) :
    EntityViewModel<Instrument.Tuning>(application, Instrument.Tuning::class.java)

class InstrumentViewModel(application: Application) : EntityViewModel<Instrument>(application, Instrument::class.java)

class PresetViewModel(application: Application) : EntityViewModel<InstrumentPreset>(application, InstrumentPreset::class.java)

class ScaleViewModel(application: Application) : EntityViewModel<Scale.Type>(application, Scale.Type::class.java)