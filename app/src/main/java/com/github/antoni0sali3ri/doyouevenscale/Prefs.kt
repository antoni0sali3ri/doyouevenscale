package com.github.antoni0sali3ri.doyouevenscale

import androidx.appcompat.app.AppCompatDelegate
import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.KPrefFactory

internal const val appId = BuildConfig.APPLICATION_ID

class Prefs(factory: KPrefFactory) {
    val core: CorePrefs = CorePrefs(factory)
    val appearance: AppearancePrefs = AppearancePrefs(factory)
}

class CorePrefs(factory: KPrefFactory) : KPref("$appId.core.prefs", factory) {
    val firstRun: Boolean by kprefSingle("FIRST_RUN")

    var keepAwake: Boolean by kpref("KEEP_AWAKE", true)
}

class AppearancePrefs(factory: KPrefFactory) : KPref("$appId.appearance.prefs", factory) {
    var fretboardColor: Int by kpref("COLOR_FRETBOARD", 0xfecd22)
    var stringColor: Int by kpref("COLOR_STRING", 0x888888)
    var fretColor: Int by kpref("COLOR_FRET", 0xababab)

    var appTheme: Int by kpref("APP_THEME", AppTheme.Device.ordinal)
}

sealed class AppTheme(val nameRes: Int, val ordinal: Int, val mode: Int) {
    object Light : AppTheme(R.string.app_theme_light, 0, AppCompatDelegate.MODE_NIGHT_NO)
    object Dark : AppTheme(R.string.app_theme_dark, 1, AppCompatDelegate.MODE_NIGHT_YES)
    object Device :
        AppTheme(R.string.app_theme_device, 2, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    companion object {
        val values by lazy {
            listOf(Light, Dark, Device)
        }

        operator fun invoke(ordinal: Int) = when (ordinal) {
            0 -> Light
            1 -> Dark
            2 -> Device
            else -> throw IllegalArgumentException()
        }
    }
}

