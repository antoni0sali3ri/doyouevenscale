package com.github.antoni0sali3ri.doyouevenscale

import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.KPrefFactory

internal val appId = BuildConfig.APPLICATION_ID

const val LIST_SEP = ";;"

class Prefs(factory: KPrefFactory) {
    val core: CorePrefs = CorePrefs(factory)
    val appearance: AppearancePrefs = AppearancePrefs(factory)
}

class CorePrefs(factory: KPrefFactory) : KPref("$appId.core.prefs", factory) {
    val firstRun: Boolean by kprefSingle("FIRST_RUN")
}

class AppearancePrefs(factory: KPrefFactory) : KPref("$appId.appearance.prefs", factory) {
    var fretboardColor : Int by kpref("COLOR_FRETBOARD", 0xfecd22)
    var stringColor: Int by kpref("COLOR_STRING", 0x888888)
    var fretColor: Int by kpref("COLOR_FRET", 0xababab)
}

