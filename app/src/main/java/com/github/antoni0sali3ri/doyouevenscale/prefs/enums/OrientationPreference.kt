package com.github.antoni0sali3ri.doyouevenscale.prefs.enums

import android.content.pm.ActivityInfo
import com.github.antoni0sali3ri.doyouevenscale.R

enum class OrientationPreference(val nameRes: Int, val mode: Int) {
    Portrait(R.string.orientation_portrait, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT),
    Landscape(R.string.orientation_landscape, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE),
    Device(R.string.orientation_device, ActivityInfo.SCREEN_ORIENTATION_USER);

    companion object {
        operator fun invoke(ordinal: Int) = when (ordinal) {
            in values().indices -> values()[ordinal]
            else -> throw IllegalArgumentException()
        }
    }
}