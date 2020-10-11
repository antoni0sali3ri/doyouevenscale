package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.github.antoni0sali3ri.doyouevenscale.MyApp
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ui.main.InstrumentConfigPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(savedInstanceState = $savedInstanceState)")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        MyApp.initialize(this)

        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        loadInstruments()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_favorite -> {
            startActivity(Intent(this, DataManagerActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun loadInstruments() {
        val configs = MyApp.getDatabase(this).instrumentPresetDao().getInstrumentPresetTabs()
        configs.observe(this, {
            val sectionsPagerAdapter = InstrumentConfigPagerAdapter(it, supportFragmentManager)
            viewPager.adapter = sectionsPagerAdapter
            tabs.setupWithViewPager(viewPager)
        })
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
        val ARG_PAGE_NUMBER = "page_number"
    }
}