package com.github.antoni0sali3ri.doyouevenscale

import android.content.Context
import ca.allanwang.kau.kpref.KPrefFactoryAndroid
import ca.allanwang.kau.kpref.KPrefFactoryInMemory
import ca.allanwang.kau.utils.ctxCoroutine
import com.github.antoni0sali3ri.doyouevenscale.core.db.ApplicationDatabase
import com.github.antoni0sali3ri.doyouevenscale.core.db.Predef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ScaleViewerApplication {

    private var prefs: Prefs? = null

    fun getPrefs(context: Context): Prefs {
        if (prefs == null) {
            initializePrefs(context)
        }
        return prefs ?: throw NullPointerException("Prefs is null! It really shouldn't be.")
    }

    fun initialize(context: Context) {
        val prefs = getPrefs(context)
        if (prefs.core.firstRun) {
            populateDatabase(context)
        }
    }

    private fun initializePrefs(context: Context) {
        prefs = if (BuildConfig.DEBUG) {
            Prefs(KPrefFactoryInMemory)
        } else {
            Prefs(KPrefFactoryAndroid(context.applicationContext))
        }
    }

    fun resetDatabase(context: Context) {
        ApplicationDatabase.getDatabase(context.applicationContext).clearAllTables()
        populateDatabase(context.applicationContext)
    }

    private fun populateDatabase(context: Context) {
        val database = ApplicationDatabase.getDatabase(context.applicationContext)
        val predef = Predef(context.applicationContext)
        context.ctxCoroutine.launch(Dispatchers.IO) {
            with(database) {
                instrumentDao().insertMultiple(predef.instruments)
                tuningDao().insertMultiple(predef.tunings)
                scaleDao().insertMultiple(predef.scaleTypes)
                instrumentPresetDao().insertMultiple(predef.configs)
            }
        }
    }
}