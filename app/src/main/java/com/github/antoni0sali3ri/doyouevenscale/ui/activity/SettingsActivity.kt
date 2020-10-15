package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import ca.allanwang.kau.kpref.activity.CoreAttributeContract
import ca.allanwang.kau.kpref.activity.KPrefActivity
import ca.allanwang.kau.kpref.activity.KPrefAdapterBuilder
import ca.allanwang.kau.utils.materialDialog
import ca.allanwang.kau.utils.string
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.github.antoni0sali3ri.doyouevenscale.AppTheme
import com.github.antoni0sali3ri.doyouevenscale.Orientation
import com.github.antoni0sali3ri.doyouevenscale.R

class SettingsActivity : KPrefActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        applyOrientation()
    }

    override fun kPrefCoreAttributes(): CoreAttributeContract.() -> Unit = {
    }

    override fun onCreateKPrefs(savedInstanceState: Bundle?): KPrefAdapterBuilder.() -> Unit = {
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
            textGetter = { string(Orientation(it).nameRes) }
            onClick = {
                materialDialog {
                    title(R.string.prefs_title_screen_orientation)
                    listItemsSingleChoice(
                        items = Orientation.values.map { string(it.nameRes) },
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

        header(R.string.prefs_category_appearance)

        text(
            title = R.string.prefs_title_app_theme,
            getter = prefs.appearance::appTheme,
            setter = { prefs.appearance.appTheme = it }
        ) {
            textGetter = { string(AppTheme(it).nameRes) }
            descRes = R.string.prefs_description_app_theme
            onClick = {
                materialDialog {
                    title(R.string.prefs_title_app_theme)
                    listItemsSingleChoice(
                        items = AppTheme.values.map { string(it.nameRes) },
                        initialSelection = item.pref
                    ) { _, index, _ ->
                        if (index != item.pref) {
                            item.pref = index
                            AppCompatDelegate.setDefaultNightMode(AppTheme(index).mode)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun launch(fromActivity: Activity) {
            ContextCompat.startActivity(
                fromActivity,
                Intent(fromActivity, SettingsActivity::class.java),
                null
            )
        }
    }
}