package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.github.antoni0sali3ri.doyouevenscale.AppTheme
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ScaleViewerApplication
import com.github.antoni0sali3ri.doyouevenscale.core.db.ApplicationDatabase
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.InstrumentPresetViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        ScaleViewerApplication.initialize(this)
        AppCompatDelegate.setDefaultNightMode(AppTheme(prefs.appearance.appTheme).mode)

        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        loadInstruments()
    }

    override fun onResume() {
        super.onResume()

        applyOrientation()

        if (prefs.core.keepAwake)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.actionManageData -> {
            DataManagerActivity.launch(this)
            true
        }
        R.id.actionSettings -> {
            SettingsActivity.launch(this)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun loadInstruments() {
        val configs = ApplicationDatabase
            .getDatabase(this)
            .instrumentPresetDao()
            .getInstrumentPresetTabs()

        configs.observe(this, { presets ->
            val sectionsPagerAdapter = InstrumentPresetViewPagerAdapter(this, presets)
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, pos ->
                tab.text = presets[pos].name
            }.attach()
        })
    }
}