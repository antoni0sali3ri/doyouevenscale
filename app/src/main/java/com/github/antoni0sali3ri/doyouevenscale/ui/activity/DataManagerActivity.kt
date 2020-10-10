package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.github.antoni0sali3ri.doyouevenscale.MyApp
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ui.main.DataManagerPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataManagerActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(savedInstanceState = $savedInstanceState)")
        super.onCreate(null)
        setContentView(R.layout.activity_data_manager)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = DataManagerPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
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
            (viewPager.adapter as DataManagerPagerAdapter).getFragmentAt(position)?.createItem()
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

}