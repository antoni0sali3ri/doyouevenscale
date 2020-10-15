package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.Orientation
import com.github.antoni0sali3ri.doyouevenscale.Prefs
import com.github.antoni0sali3ri.doyouevenscale.ScaleViewerApplication

val AppCompatActivity.prefs: Prefs get() = ScaleViewerApplication.getPrefs(this)

fun AppCompatActivity.debug(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message)
    }
}

fun AppCompatActivity.applyOrientation() {
    if (prefs.core.orientationIsGlobal || this is MainActivity) {
        requestedOrientation = Orientation(prefs.core.orientation).mode
    } else if (!prefs.core.orientationIsGlobal) {
        requestedOrientation = Orientation.Device.mode
    }
}