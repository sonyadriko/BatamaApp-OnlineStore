package com.example.tokoonline.view.activity.toko.pesanan

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.invisible
import com.example.tokoonline.view.activity.toko.pesanan.SectionsPagerAdapter
import com.example.tokoonline.databinding.ActivityStatusPesananBinding

class StatusPesananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatusPesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStatusPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.binding.divider.invisible()
        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }
}