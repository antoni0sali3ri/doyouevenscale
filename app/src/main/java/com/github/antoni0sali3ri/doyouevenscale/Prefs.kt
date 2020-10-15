package com.github.antoni0sali3ri.doyouevenscale

import android.content.pm.ActivityInfo
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
    var orientation: Int by kpref("SCREEN_ORIENTATION", Orientation.Device.ordinal)
    var orientationIsGlobal: Boolean by kpref("SCREEN_ORIENTATION_GLOBAL", false)
}

class AppearancePrefs(factory: KPrefFactory) : KPref("$appId.appearance.prefs", factory) {
    var fretboardColor: Int by kpref("COLOR_FRETBOARD", 0xfecd22)
    var stringColor: Int by kpref("COLOR_STRING", 0x888888)
    var fretColor: Int by kpref("COLOR_FRET", 0xababab)

    var appTheme: Int by kpref("APP_THEME", AppTheme.Device.ordinal)
}

sealed class AppTheme(val ordinal: Int, val nameRes: Int, val mode: Int) {
    object Light : AppTheme(0, R.string.app_theme_light, AppCompatDelegate.MODE_NIGHT_NO)
    object Dark : AppTheme(1, R.string.app_theme_dark, AppCompatDelegate.MODE_NIGHT_YES)
    object Device :
        AppTheme(2, R.string.app_theme_device, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    companion object {
        val values by lazy {
            listOf(Light, Dark, Device)
        }

        operator fun invoke(ordinal: Int) = when (ordinal) {
            in 0..2 -> values[ordinal]
            else -> throw IllegalArgumentException()
        }
    }
}

sealed class Orientation(val ordinal: Int, val nameRes: Int, val mode: Int) {
    object Portrait : Orientation(
        0,
        R.string.orientation_portrait,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    )

    object Landscape : Orientation(
        1,
        R.string.orientation_landscape,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    )

    object Device :
        Orientation(2, R.string.orientation_device, ActivityInfo.SCREEN_ORIENTATION_USER)

    companion object {
        val values by lazy {
            listOf(Portrait, Landscape, Device)
        }

        operator fun invoke(ordinal: Int) = when (ordinal) {
            in 0..2 -> values[ordinal]
            else -> throw IllegalArgumentException()
        }
    }
}

