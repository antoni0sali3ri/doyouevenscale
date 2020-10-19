package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.content.ContextCompat
import ca.allanwang.kau.about.kauLaunchAbout
import ca.allanwang.kau.kpref.activity.CoreAttributeContract
import ca.allanwang.kau.kpref.activity.KPrefActivity
import ca.allanwang.kau.kpref.activity.KPrefAdapterBuilder
import ca.allanwang.kau.xml.showChangelog
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ui.settings.getAppearanceSettings
import com.github.antoni0sali3ri.doyouevenscale.ui.settings.getCoreSettings
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial

class SettingsActivity : KPrefActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        applyOrientation()
    }

    override fun kPrefCoreAttributes(): CoreAttributeContract.() -> Unit = {
        accentColor = { resources.getColor(R.color.colorAccent) }
    }

    override fun onCreateKPrefs(savedInstanceState: Bundle?): KPrefAdapterBuilder.() -> Unit = {
        subItems(R.string.prefs_category_core, getCoreSettings()) {
            descRes = R.string.prefs_category_description_core
            iicon = GoogleMaterial.Icon.gmd_settings
        }

        subItems(R.string.prefs_category_appearance, getAppearanceSettings()) {
            descRes = R.string.prefs_category_description_appearance
            iicon = GoogleMaterial.Icon.gmd_palette
        }

        plainText(R.string.prefs_title_about) {
            descRes = R.string.prefs_description_about
            iicon = GoogleMaterial.Icon.gmd_info
            onClick = {
                kauLaunchAbout<AboutActivity>()
            }
        }

        plainText(R.string.prefs_title_changelog) {
            descRes = R.string.prefs_description_changelog
            iicon = GoogleMaterial.Icon.gmd_history
            onClick = {
                showChangelog(R.xml.changelog)
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