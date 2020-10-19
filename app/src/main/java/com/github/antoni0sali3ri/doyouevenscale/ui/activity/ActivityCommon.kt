package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.ScaleViewerApplication
import com.github.antoni0sali3ri.doyouevenscale.prefs.OrientationPreference
import com.github.antoni0sali3ri.doyouevenscale.prefs.Prefs

val AppCompatActivity.prefs: Prefs get() = ScaleViewerApplication.getPrefs(this)

val Fragment.prefs: Prefs get() = ScaleViewerApplication.getPrefs(requireContext())

fun AppCompatActivity.debug(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message)
    }
}

fun AppCompatActivity.applyOrientation() {
    if (prefs.core.orientationIsGlobal || this is MainActivity) {
        requestedOrientation = OrientationPreference(prefs.core.orientation).mode
    } else if (!prefs.core.orientationIsGlobal) {
        requestedOrientation = OrientationPreference.Device.mode
    }
}