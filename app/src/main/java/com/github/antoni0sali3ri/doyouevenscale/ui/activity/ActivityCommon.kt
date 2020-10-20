package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.ScaleViewerApplication
import com.github.antoni0sali3ri.doyouevenscale.prefs.Prefs
import com.github.antoni0sali3ri.doyouevenscale.prefs.enums.OrientationPreference

val AppCompatActivity.prefs: Prefs get() = ScaleViewerApplication.getPrefs(this)

val Fragment.prefs: Prefs get() = ScaleViewerApplication.getPrefs(requireContext())

fun AppCompatActivity.debug(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message)
    }
}

fun Fragment.debug(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message)
    }
}

fun Context.toast(length: Int, msgRes: Int) {
    Toast.makeText(this, msgRes, length).show()
}

fun Context.toastShort(msgRes: Int) {
    toast(Toast.LENGTH_SHORT, msgRes)
}

fun Context.toastLong(msgRes: Int) {
    toast(Toast.LENGTH_LONG, msgRes)
}

fun AppCompatActivity.applyOrientation() {
    if (prefs.core.orientationIsGlobal || this is MainActivity) {
        requestedOrientation = OrientationPreference(prefs.core.orientation).mode
    } else if (!prefs.core.orientationIsGlobal) {
        requestedOrientation = OrientationPreference.Device.mode
    }
}