package com.github.antoni0sali3ri.doyouevenscale.prefs

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatDelegate
import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.KPrefFactory
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.ConstantFretSpacing
import com.github.antoni0sali3ri.doyouevenscale.core.model.EqualTemperamentFretSpacing
import com.github.antoni0sali3ri.doyouevenscale.core.model.FretSpacing

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
}

class AppearancePrefs(factory: KPrefFactory) : KPref("$appId.appearance.prefs", factory) {
    var fretboardColor: Int by kpref("COLOR_FRETBOARD", 0xfecd22)
    var stringColor: Int by kpref("COLOR_STRING", 0x888888)
    var fretColor: Int by kpref("COLOR_FRET", 0xababab)

    var appTheme: Int by kpref("APP_THEME", AppThemePreference.Device.ordinal)
    var fretSpacing: Int by kpref("FRET_SPACING", FretSpacingPreference.Constant.ordinal)
}

sealed class AppThemePreference(val ordinal: Int, val nameRes: Int, val mode: Int) {
    object Light : AppThemePreference(0, R.string.app_theme_light, AppCompatDelegate.MODE_NIGHT_NO)
    object Dark : AppThemePreference(1, R.string.app_theme_dark, AppCompatDelegate.MODE_NIGHT_YES)
    object Device :
        AppThemePreference(2, R.string.app_theme_device, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

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

sealed class OrientationPreference(val ordinal: Int, val nameRes: Int, val mode: Int) {
    object Portrait : OrientationPreference(
        0,
        R.string.orientation_portrait,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    )

    object Landscape : OrientationPreference(
        1,
        R.string.orientation_landscape,
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    )

    object Device :
        OrientationPreference(2, R.string.orientation_device, ActivityInfo.SCREEN_ORIENTATION_USER)

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

sealed class FretSpacingPreference(val ordinal: Int, val nameRes: Int, val mode: FretSpacing) {
    object Constant : FretSpacingPreference(0, R.string.fret_spacing_constant, ConstantFretSpacing)
    object EqualTemperament : FretSpacingPreference(
        1,
        R.string.fret_spacing_equal_temperament,
        EqualTemperamentFretSpacing
    )

    companion object {
        val values by lazy {
            listOf(Constant, EqualTemperament)
        }

        operator fun invoke(ordinal: Int) = when (ordinal) {
            in 0..1 -> values[ordinal]
            else -> throw IllegalArgumentException()
        }
    }
}

