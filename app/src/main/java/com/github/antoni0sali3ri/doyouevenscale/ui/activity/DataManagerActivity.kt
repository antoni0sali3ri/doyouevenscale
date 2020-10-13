package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.github.antoni0sali3ri.doyouevenscale.MyApp
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.DataManagerViewPagerAdapter
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list.EntityListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DataManagerActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_data_manager)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = DataManagerViewPagerAdapter(this)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, pos ->
            tab.text = resources.getString(TAB_TITLES[pos])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_dm_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.action_create_entity -> {
            val position = viewPager.currentItem
            // Special thank you to the Android SDK for this goddamn code smell
            val fragmentTag = "f$position"
            val fragment =
                supportFragmentManager.findFragmentByTag(fragmentTag) as EntityListFragment<*>?
            fragment?.createItem()
            true
        }
        R.id.action_restore_defaults -> {
            lifecycleScope.launch(Dispatchers.IO) {
                MyApp.resetDatabase(this@DataManagerActivity)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        val TAB_TITLES = listOf(
            R.string.tab_title_dm_presets,
            R.string.tab_title_dm_instruments,
            R.string.tab_title_dm_tunings,
            R.string.tab_title_dm_scales
        )

    }
}