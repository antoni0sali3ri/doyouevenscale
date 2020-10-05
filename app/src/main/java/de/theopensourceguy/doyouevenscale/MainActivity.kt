package de.theopensourceguy.doyouevenscale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.theopensourceguy.doyouevenscale.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyApp.initialize(this)

        viewPager = findViewById(R.id.view_pager)
        tabs= findViewById(R.id.tabs)
        loadInstruments()
    }

    private fun loadInstruments() {
        val ids = MyApp.prefs.core.getInstrumentIdList()
        val configs = MyApp.database.instrumentConfigDao().getInstrumentConfigsByIds(ids)
        configs.observe(this, {
            val instruments = MyApp.database.instrumentDao().getInstrumentsByIds(it.map { it.instrumentId })
            instruments.observe(this, {
                val sectionsPagerAdapter = SectionsPagerAdapter(it, this, supportFragmentManager)
                viewPager.adapter = sectionsPagerAdapter
                tabs.setupWithViewPager(viewPager)
            })
        })
    }
}