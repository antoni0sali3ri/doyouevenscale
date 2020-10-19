package com.github.antoni0sali3ri.doyouevenscale.prefs

import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.KPrefFactory
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.prefs.enums.AppThemePreference
import com.github.antoni0sali3ri.doyouevenscale.prefs.enums.FretSpacingPreference
import com.github.antoni0sali3ri.doyouevenscale.prefs.enums.OrientationPreference

internal const val appId = BuildConfig.APPLICATION_ID

class Prefs(factory: KPrefFactory) {
    val core: CorePrefs = CorePrefs(factory)
    val appearance: AppearancePrefs = AppearancePrefs(factory)
}

class CorePrefs(factory: KPrefFactory) : KPref("$appId.core.prefs", factory) {
    val firstRun: Boolean by kprefSingle("FIRST_RUN")
    var keepAwake: Boolean by kpref("KEEP_AWAKE", false)
    var orientation: Int by kpref("SCREEN_ORIENTATION", OrientationPreference.Device.ordinal)
    var orientationIsGlobal: Boolean by kpref("SCREEN_ORIENTATION_GLOBAL", false)
    var startInFullScreen: Boolean by kpref("START_IN_FULL_SCREEN_MODE", false)
}

class AppearancePrefs(factory: KPrefFactory) : KPref("$appId.appearance.prefs", factory) {
    var appTheme: Int by kpref("APP_THEME", AppThemePreference.Device.ordinal)
    var fretSpacing: Int by kpref("FRET_SPACING", FretSpacingPreference.EqualTemperament.ordinal)
}

