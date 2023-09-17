package com.example.tokoonline.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityMainBinding
import com.example.tokoonline.view.fragment.AkunFragment
import com.example.tokoonline.view.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tokoonline.view.fragment.RiwayatTransaksiFragment

class MainActivity : BaseActivity() {

    private lateinit var active: Fragment

    private lateinit var menu: Menu
    private lateinit var menuitem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNav()
    }

    private fun setUpBottomNav() {
        val fragmentHome: Fragment = HomeFragment()
        val fragmentRiwayattransaksi: Fragment = RiwayatTransaksiFragment()
        val fragmentAkun: Fragment = AkunFragment()

        active = fragmentHome
        supportFragmentManager.apply {
            beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
            beginTransaction().add(R.id.container, fragmentRiwayattransaksi)
                .hide(fragmentRiwayattransaksi).commit()
            beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()
        }

        bottomNavigationView = binding.navView
        menu = bottomNavigationView.menu
        menuitem = menu.getItem(0)
        menuitem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    callFragment(0, fragmentHome)
                }

                R.id.navigation_riwayattransaksi -> {
                    callFragment(1, fragmentRiwayattransaksi)
                }

                R.id.navigation_akun -> {
                    callFragment(2, fragmentAkun)
                }
            }

            false
        }
    }

    private fun callFragment(int: Int, fragment: Fragment) {
        menuitem = menu.getItem(int)
        menuitem.isChecked = true
        supportFragmentManager.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}