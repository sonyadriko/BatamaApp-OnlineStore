package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.OnItemClickListener
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.repository.KeranjangRepository
import com.example.tokoonline.databinding.ActivityKeranjangBinding
import com.example.tokoonline.view.adapter.AdapterKeranjang
import com.example.tokoonline.view.viewmodel.KeranjangViewModel
import kotlinx.coroutines.launch

class KeranjangActivity : BaseActivity() {

    private var uuid = ""
    private val viewModel: KeranjangViewModel by viewModels()

    private lateinit var binding: ActivityKeranjangBinding
    private lateinit var keranjangRepository: KeranjangRepository
    private val adapter: AdapterKeranjang by lazy {
        AdapterKeranjang(viewModel, object : OnItemClickListener {
            override fun onItemClick(data: Any, position: Int) {
                showProgressDialog()
                val item = data as AdapterKeranjang.Companion.ItemData
                if (item.item.jumlah <= 0) { // delete
                    keranjangRepository.removeKeranjang(uuid, item.item) {
                        dismissProgressDialog()
                        if (!it) {
                            showToast("gagal update produk dalam keranjang")
                            finish()
                        }
                    }
                } else {
                    keranjangRepository.updateKeranjang(uuid, item.item) {
                        dismissProgressDialog()
                        if (!it) {
                            showToast("gagal update produk dalam keranjang")
                            finish()
                        }
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeranjangBinding.inflate(layoutInflater)
        keranjangRepository = KeranjangRepository(this)
        setContentView(binding.root)

        binding.btnBayar.setOnClickListener{
            val totalBelanja = viewModel.totalBelanja.value ?: 0
            goToBayar(totalBelanja)
        }

        initView()
        lifecycleScope.launch {
            userRepository.uid?.let {
                uuid = it
                getKeranjang(uuid)
            }
        }
    }

    private fun initView() {
        binding.rvProduk.adapter = adapter

        viewModel.totalBelanja.observe(this) { total ->
            binding.tvTotal.text = moneyFormatter(total)
        }

    }

    private suspend fun getKeranjang(it: String) {
        showProgressDialog()

        keranjangRepository.getKeranjang(userUid = it).collect {
            dismissProgressDialog()
            if (it.isEmpty() || it[0] == null) {
                showToast("Keranjang kosong")
            } else adapter.submitList(it)
        }
    }
}