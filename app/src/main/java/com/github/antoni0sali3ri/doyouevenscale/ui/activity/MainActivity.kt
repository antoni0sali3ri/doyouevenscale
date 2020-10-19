package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ScaleViewerApplication
import com.github.antoni0sali3ri.doyouevenscale.core.db.ApplicationDatabase
import com.github.antoni0sali3ri.doyouevenscale.prefs.enums.AppThemePreference
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.InstrumentPresetViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var appbarLayout: AppBarLayout
    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager2

    private var fullScreen: Boolean = false //prefs.core.startInFullScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        ScaleViewerApplication.initialize(this)
        AppCompatDelegate.setDefaultNightMode(AppThemePreference(prefs.appearance.appTheme).mode)

        fullScreen = prefs.core.startInFullScreen

        appbarLayout = findViewById(R.id.appbarLayout)
        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)

        loadInstruments()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean(KEY_FULL_SCREEN, fullScreen)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.apply {
            fullScreen = getBoolean(KEY_FULL_SCREEN)
        }
    }

    override fun onResume() {
        super.onResume()

        applyOrientation()
        applyFullScreen()

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

    fun toggleFullScreen() {
        fullScreen = !fullScreen
    }

    fun applyFullScreen() {
        appbarLayout.visibility = if (fullScreen) View.GONE else View.VISIBLE
    }

    private fun loadInstruments() {
        val configs = ApplicationDatabase
            .getDatabase(this)
            .instrumentPresetDao()
            .getInstrumentPresetTabs()

        configs.observe(this, { presets ->
            val adapter = InstrumentPresetViewPagerAdapter(this, presets)
            //Override full screen setting when there are no presets configured as tabs
            if (adapter.itemCount == 0) {
                fullScreen = false
                applyFullScreen()
            }
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, pos ->
                tab.text = presets[pos].name
            }.attach()
        })
    }

    companion object {
        val KEY_FULL_SCREEN = "full_screen"
    }

    inner class FullscreenTouchListener : View.OnTouchListener {

        private val gestureDetector =
            GestureDetector(this@MainActivity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean = true

                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    toggleFullScreen()
                    applyFullScreen()
                    return true
                }
            })

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
        }
    }
}
