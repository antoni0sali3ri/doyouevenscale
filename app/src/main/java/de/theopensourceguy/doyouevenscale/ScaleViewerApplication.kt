package de.theopensourceguy.doyouevenscale

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import ca.allanwang.kau.kpref.KPrefFactoryInMemory
import de.theopensourceguy.doyouevenscale.core.db.ApplicationDatabase
import de.theopensourceguy.doyouevenscale.core.db.DbUtils
import de.theopensourceguy.doyouevenscale.core.model.Predef

object ScaleViewerApplication : Application() {
    val APP_ID = "de.theopensourceguy.ScaleViewer"
    val DB_ID = "$APP_ID.db"

    lateinit var prefs: Prefs

    lateinit var database: ApplicationDatabase

    fun initialize(context: Context) {
        //prefs = Prefs(KPrefFactoryAndroid(context))
        prefs = Prefs(KPrefFactoryInMemory)
        database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        initDatabase(context)
    }

    fun initDatabase(context: Context) {
        if (prefs.core.firstRun) {
            val predef = Predef(context)
            database.tuningDao().insertTunings(predef.tunings)
            database.scaleDao().insertScaleTypes(predef.scaleTypes)
            DbUtils(database).let {dbu ->
                val ids = predef.instruments.map {
                    dbu.insertInstrumentAndTuning(it)
                }
                Log.d("APP", "$ids")
                prefs.core.setInstrumentIdList(ids.map { it.toInt() })
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

}