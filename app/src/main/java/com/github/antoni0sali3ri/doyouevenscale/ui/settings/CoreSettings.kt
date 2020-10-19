package com.github.antoni0sali3ri.doyouevenscale.ui.settings

import ca.allanwang.kau.kpref.activity.KPrefAdapterBuilder
import ca.allanwang.kau.utils.materialDialog
import ca.allanwang.kau.utils.string
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.prefs.enums.OrientationPreference
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.SettingsActivity
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.applyOrientation
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.prefs

fun SettingsActivity.getCoreSettings(): KPrefAdapterBuilder.() -> Unit = {

    header(R.string.prefs_category_core)

    checkbox(
        title = R.string.prefs_title_keep_awake,
        getter = prefs.core::keepAwake,
        setter = { prefs.core.keepAwake = it }
    ) {
        descRes = R.string.prefs_description_keep_awake
    }

    text(
        title = R.string.prefs_title_screen_orientation,
        getter = prefs.core::orientation,
        setter = {
            prefs.core.orientation = it
            applyOrientation()
        }
    ) {
        textGetter = { string(OrientationPreference(it).nameRes) }
        onClick = {
            materialDialog {
                title(R.string.prefs_title_screen_orientation)
                listItemsSingleChoice(
                    items = OrientationPreference.values().map { string(it.nameRes) },
                    initialSelection = item.pref
                ) { _, index, _ ->
                    if (index != item.pref) {
                        item.pref = index
                    }
                }
            }
        }
    }

    checkbox(
        title = R.string.prefs_title_screen_orientation_global,
        getter = prefs.core::orientationIsGlobal,
        setter = {
            prefs.core.orientationIsGlobal = it
            applyOrientation()
        }
    ) {
        descRes = R.string.prefs_description_orientation_global
    }

    checkbox(
        title = R.string.prefs_title_full_screen,
        getter = prefs.core::startInFullScreen,
        setter = { prefs.core.startInFullScreen = it }
    ) {
        descRes = R.string.prefs_description_full_screen
    }
}