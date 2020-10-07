package de.theopensourceguy.doyouevenscale.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.ui.main.DataManagerPagerAdapter

class DataManagerActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(savedInstanceState = $savedInstanceState)")
        super.onCreate(null)
        setContentView(R.layout.activity_data_manager)
        val sectionsPagerAdapter = DataManagerPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

}