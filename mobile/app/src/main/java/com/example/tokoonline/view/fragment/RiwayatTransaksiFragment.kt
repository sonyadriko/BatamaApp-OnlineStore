package com.example.tokoonline.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.view.activity.DetailPesananTokoActivity
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.databinding.FragmentRiwayattransaksiBinding
import com.example.tokoonline.view.activity.PembayaranActivity
import com.example.tokoonline.view.adapter.AdapterRiwayat
import com.example.tokoonline.view.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch


class RiwayatTransaksiFragment : BaseFragment() {
    private var uuid = ""
    private lateinit var binding : FragmentRiwayattransaksiBinding
    private lateinit var viewModel : TransactionViewModel


    private val adapter: AdapterRiwayat by lazy {
        AdapterRiwayat(object : OnItemClick {
            override fun onClick(data: Any, position: Int) {
                if ((data as Transaction).status.equals("pending", ignoreCase = true)
                    && data.metodePembayaran.equals("cod", ignoreCase = true).not()
                ) {
                    startActivity(
                        PembayaranActivity.createIntent(
                            requireActivity(),
                            data
                        )
                    )
                } else {
                    val intent = Intent(requireActivity(), DetailPesananTokoActivity::class.java)
                    intent.putExtra("data", data)
                    startActivity(intent)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentRiwayattransaksiBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        init()

        binding.rvRiwayat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@RiwayatTransaksiFragment.adapter
        }
        binding.swipeRefresh.setOnRefreshListener {
            init()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        userRepository.uid?.let {
            uuid = it
            getRiwayat(uuid)
            init()
        }
    }

    private fun getRiwayat(userUid : String){
        binding.swipeRefresh.isRefreshing = true
        viewModel.getTransaction(userUid) { transactionList ->
            binding.swipeRefresh.isRefreshing = false
            adapter.submitList(transactionList)
            if (transactionList.isNotEmpty()) {
                binding.divGambar.gone()
                binding.rvRiwayat.visible()
            } else {
                binding.divGambar.visible()
                binding.rvRiwayat.gone()
            }
        }
    }
    private fun init() {
        lifecycleScope.launch {
            adapter.submitList(emptyList())
            userRepository.uid?.let {
                uuid = it
                getRiwayat(uuid)
            }
        }
    }}