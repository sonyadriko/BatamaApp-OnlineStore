package com.example.tokoonline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.R
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.view.adapter.AdapterRiwayat

//import kotlinx.android.synthetic.main.fragment_riwayattransaksi.rv_riwayat


/**
 * A simple [Fragment] subclass.
 */

class RiwayatTransaksiFragment : Fragment() {

    // TODO: Rename and change types of parameters

    lateinit var rvRiwayat: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_riwayattransaksi, container, false)

        rvRiwayat = view.findViewById(R.id.rv_riwayat)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rvRiwayat.adapter = AdapterRiwayat(arrRiwayat)
        rvRiwayat.layoutManager = layoutManager

        return view


    }

    val arrRiwayat = emptyList<Transaction>()
}