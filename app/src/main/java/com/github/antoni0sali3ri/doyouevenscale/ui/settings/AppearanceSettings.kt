package com.github.antoni0sali3ri.doyouevenscale.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import ca.allanwang.kau.kpref.activity.KPrefAdapterBuilder
import ca.allanwang.kau.utils.materialDialog
import ca.allanwang.kau.utils.string
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.prefs.AppThemePreference
import com.github.antoni0sali3ri.doyouevenscale.prefs.FretSpacingPreference
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.SettingsActivity
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.prefs

fun SettingsActivity.getAppearanceSettings(): KPrefAdapterBuilder.() -> Unit = {

    header(R.string.prefs_category_appearance)

    text(
        title = R.string.prefs_title_app_theme,
        getter = prefs.appearance::appTheme,
        setter = { prefs.appearance.appTheme = it }
    ) {
        textGetter = { string(AppThemePreference(it).nameRes) }
        descRes = R.string.prefs_description_app_theme
        onClick = {
            materialDialog {
                title(R.string.prefs_title_app_theme)
                listItemsSingleChoice(
                    items = AppThemePreference.values.map { string(it.nameRes) },
                    initialSelection = item.pref
                ) { _, index, _ ->
                    if (index != item.pref) {
                        item.pref = index
                        AppCompatDelegate.setDefaultNightMode(AppThemePreference(index).mode)
                    }
                }
            }
        }
    }

    header(R.string.prefs_category_appearance_fretboard)

    text(
        title = R.string.prefs_title_fret_spacing,
        getter = prefs.appearance::fretSpacing,
        setter = { prefs.appearance.fretSpacing = it }
    ) {
        textGetter = { string(FretSpacingPreference(it).nameRes) }
        descRes = R.string.prefs_description_fret_spacing
        onClick = {
            materialDialog {
                title(R.string.prefs_title_fret_spacing)
                listItemsSingleChoice(
                    items = FretSpacingPreference.values.map { string(it.nameRes) },
                    initialSelection = item.pref
                ) { _, index, _ ->
                    if (index != item.pref) {
                        item.pref = index
                    }
                }
            }
        }
    }

}