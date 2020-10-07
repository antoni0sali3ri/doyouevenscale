package de.theopensourceguy.doyouevenscale.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.theopensourceguy.doyouevenscale.MyApp
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.ui.main.InstrumentConfigPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        MyApp.initialize(this)

        viewPager = findViewById(R.id.view_pager)
        tabs= findViewById(R.id.tabs)
        loadInstruments()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_favorite -> {
            startActivity(Intent(this, DataManagerActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun loadInstruments() {
        val ids = MyApp.prefs.core.getInstrumentIdList()
        val configs = MyApp.getDatabase(this).instrumentConfigDao().getInstrumentConfigsByIds(ids)
        configs.observe(this, {
            val instruments = MyApp.getDatabase(this).instrumentDao().getInstrumentsByIds(it.map { it.instrumentId })
            instruments.observe(this, {
                val sectionsPagerAdapter = InstrumentConfigPagerAdapter(it, supportFragmentManager)
                viewPager.adapter = sectionsPagerAdapter
                tabs.setupWithViewPager(viewPager)
            })
        })
    }
}