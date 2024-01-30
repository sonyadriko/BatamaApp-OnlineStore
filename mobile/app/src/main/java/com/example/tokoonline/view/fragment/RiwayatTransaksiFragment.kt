package com.example.tokoonline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.util.*
import com.example.tokoonline.databinding.FragmentRiwayattransaksiBinding
import com.example.tokoonline.view.adapter.AdapterRiwayat
import com.example.tokoonline.view.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch


class RiwayatTransaksiFragment : BaseFragment() {
    private var uuid = ""
    private lateinit var binding : FragmentRiwayattransaksiBinding
    private lateinit var viewModel : TransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRiwayattransaksiBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]


        lifecycleScope.launch {
            userRepository.uid?.let {
                uuid = it
                getRiwayat(uuid)

            }
        }

        return binding.root
    }

    private fun getRiwayat(userUid : String){
        showProgressDialog()
        viewModel.getTransaction(userUid){transactionList ->

            dismissProgressDialog()
            val recyclerView : RecyclerView = binding.rvRiwayat
            val adapter = AdapterRiwayat(transactionList)

            if (transactionList.isNotEmpty()){
                binding.divGambar.gone()
                binding.rvRiwayat.visible()
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            } else {
                binding.divGambar.visible()
                binding.rvRiwayat.gone()
            }
        }
    }
}