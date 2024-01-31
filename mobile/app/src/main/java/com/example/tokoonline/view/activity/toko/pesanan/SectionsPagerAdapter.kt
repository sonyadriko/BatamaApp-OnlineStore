package com.example.tokoonline.view.activity.toko.pesanan

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tokoonline.view.fragment.toko.pesanan.StatusPesananFragment

private val tabTitles = arrayOf(
    "Perlu Dikirim",
    "Pembatalan",
    "Selesai",
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> StatusPesananFragment.newInstance(position)
            2 -> StatusPesananFragment.newInstance(position)
            3 -> StatusPesananFragment.newInstance(position)
            else -> {
                StatusPesananFragment.newInstance(position)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getCount(): Int {
        return 3
    }
}