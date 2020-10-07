package de.theopensourceguy.doyouevenscale

import android.app.Application
import android.content.Context
import androidx.room.Room
import ca.allanwang.kau.kpref.KPrefFactoryAndroid
import ca.allanwang.kau.kpref.KPrefFactoryInMemory
import de.theopensourceguy.doyouevenscale.core.db.ApplicationDatabase
import de.theopensourceguy.doyouevenscale.core.model.Predef

object MyApp : Application() {
    val APP_ID = "de.theopensourceguy.ScaleViewer"
    val DB_ID = "$APP_ID.db"

    lateinit var prefs: Prefs

    private var database: ApplicationDatabase? = null

    fun getDatabase(context: Context) : ApplicationDatabase {
        if (database == null) {
            initializeDatabase(context)
        }
        return database!!
    }

    fun initialize(context: Context) {
        initializePrefs(context)
        populateDatabase(context)
    }

    private fun initializePrefs(context: Context) {
        if (BuildConfig.DEBUG) {
            prefs = Prefs(KPrefFactoryInMemory)
        } else {
            prefs = Prefs(KPrefFactoryAndroid(context))
        }
    }

    private fun initializeDatabase(context: Context) {
        if (BuildConfig.DEBUG) {
            database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        } else {
            database = Room
                .databaseBuilder(context, ApplicationDatabase::class.java, "${BuildConfig.APPLICATION_ID}.db")
                .build()
        }

    }

    private fun populateDatabase(context: Context) {
        val database = getDatabase(context)
        if (prefs.core.firstRun) {
            val predef = Predef(context)
            database.tuningDao().insertTunings(predef.tunings)
            database.scaleDao().insertScaleTypes(predef.scaleTypes)
            database.instrumentDao().insertInstruments(predef.instruments)
            val ids = database.instrumentConfigDao().insertInstrumentConfigs(predef.configs)
            prefs.core.setInstrumentIdList(ids)
        }
    }
}