package de.theopensourceguy.doyouevenscale

import android.app.Application
import android.content.Context
import androidx.room.Room
import ca.allanwang.kau.kpref.KPrefFactoryAndroid
import ca.allanwang.kau.kpref.KPrefFactoryInMemory
import ca.allanwang.kau.utils.ctxCoroutine
import de.theopensourceguy.doyouevenscale.core.db.ApplicationDatabase
import de.theopensourceguy.doyouevenscale.core.model.Predef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MyApp : Application() {
    val APP_ID = "de.theopensourceguy.ScaleViewer"
    val DB_ID = "$APP_ID.db"

    private var prefs: Prefs? = null

    @Volatile
    private var database: ApplicationDatabase? = null

    fun getDatabase(context: Context): ApplicationDatabase {
        if (database == null) synchronized(this) {
            initializeDatabase(context)
            return database!!
        }
        else
            return database!!
    }

    fun getPrefs(context: Context): Prefs {
        if (prefs == null) {
            initializePrefs(context)
        }
        return prefs!!
    }

    fun initialize(context: Context) {
        val prefs = getPrefs(context)
        if (prefs.core.firstRun)
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
                //.allowMainThreadQueries()
                .build()
        } else {
            database = Room
                .databaseBuilder(
                    context,
                    ApplicationDatabase::class.java,
                    "${BuildConfig.APPLICATION_ID}.db"
                )
                .build()
        }
    }

    fun resetDatabase(context: Context) {
        getDatabase(context).clearAllTables()
        populateDatabase(context)
    }

    private fun populateDatabase(context: Context) {
        val database = getDatabase(context)
        val predef = Predef(context)
        context.ctxCoroutine.launch(Dispatchers.IO) {
            with (database) {
                tuningDao().insertTunings(predef.tunings)
                scaleDao().insertScaleTypes(predef.scaleTypes)
                instrumentDao().insertInstruments(predef.instruments)
                instrumentConfigDao().insertInstrumentPresets(predef.configs)
            }
        }
    }
}