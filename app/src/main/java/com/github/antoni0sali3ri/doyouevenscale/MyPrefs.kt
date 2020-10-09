package com.github.antoni0sali3ri.doyouevenscale

import android.util.Log
import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.KPrefFactory

internal val appId = MyApp.APP_ID

const val LIST_SEP = ";;"

class Prefs(factory: KPrefFactory) {
    val core: CorePrefs = CorePrefs(factory)
    val appearance: AppearancePrefs = AppearancePrefs(factory)
}

class CorePrefs(factory: KPrefFactory) : KPref("$appId.core.prefs", factory) {
    val firstRun: Boolean by kprefSingle("FIRST_RUN")

    var instrumentIdsForTabs: String by kpref("INSTRUMENT_IDS", "0")

    fun getInstrumentIdList() : List<Long> {
        val res = instrumentIdsForTabs.decodeLongList()
        Log.d("Prefs", "getInstrumentIdList() = $res")
        return res
    }

    fun setInstrumentIdList(ids: List<Long>) {
        Log.d("Prefs", "setInstrumentList($ids)")
        instrumentIdsForTabs = ids.encodeAsString()
        Log.d("Prefs", "instrumentIdsForTabs = $instrumentIdsForTabs")
    }
}

class AppearancePrefs(factory: KPrefFactory) : KPref("$appId.appearance.prefs", factory) {
    var fretboardColor : Int by kpref("COLOR_FRETBOARD", 0xfecd22)
    var stringColor: Int by kpref("COLOR_STRING", 0x888888)
    var fretColor: Int by kpref("COLOR_FRET", 0xababab)

    var tabs: Set<String> by kpref("DISPLAYED_TABS", emptySet())
}

fun List<Long>.encodeAsString() = joinToString(LIST_SEP)

fun String.decodeLongList() : List<Long> = split(LIST_SEP).filter {!it.isBlank()}.map { it.toLong() }

