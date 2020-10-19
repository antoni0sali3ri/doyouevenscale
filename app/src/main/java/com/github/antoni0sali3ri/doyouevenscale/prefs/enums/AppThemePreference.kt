package com.github.antoni0sali3ri.doyouevenscale.prefs.enums

import androidx.appcompat.app.AppCompatDelegate
import com.github.antoni0sali3ri.doyouevenscale.R

enum class AppThemePreference(val nameRes: Int, val mode: Int) {
    Light(R.string.app_theme_light, AppCompatDelegate.MODE_NIGHT_NO),
    Dark(R.string.app_theme_dark, AppCompatDelegate.MODE_NIGHT_YES),
    Device(R.string.app_theme_device, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        operator fun invoke(ordinal: Int) = when (ordinal) {
            in values().indices -> values()[ordinal]
            else -> throw IllegalArgumentException()
        }
    }
}