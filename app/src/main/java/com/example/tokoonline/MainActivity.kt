package com.example.tokoonline

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.view.fragment.AkunFragment
import com.example.tokoonline.view.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tokoonline.view.fragment.RiwayatTransaksiFragment

class MainActivity : BaseActivity() {

    val fragmentHome: Fragment = HomeFragment()
    val fragmentRiwayattransaksi: Fragment = RiwayatTransaksiFragment()
    val fragmentAkun: Fragment = AkunFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuitem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpBottomNav()
    }

    fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentRiwayattransaksi)
            .hide(fragmentRiwayattransaksi).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
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

    fun callFragment(int: Int, fragment: Fragment) {
        menuitem = menu.getItem(int)
        menuitem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}