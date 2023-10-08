package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.OnItemClickListener
import com.example.tokoonline.data.model.ProdukKeranjang
import com.example.tokoonline.data.repository.KeranjangRepository
import com.example.tokoonline.databinding.ActivityKeranjangBinding
import com.example.tokoonline.view.adapter.AdapterKeranjang
import kotlinx.coroutines.launch

class KeranjangActivity : BaseActivity() {

    private var uuid = ""

    private lateinit var binding: ActivityKeranjangBinding
    private lateinit var keranjangRepository: KeranjangRepository
    private val adapter: AdapterKeranjang by lazy {
        AdapterKeranjang(object : OnItemClickListener {
            override fun onItemClick(data: Any, position: Int) {
                val item = data as ProdukKeranjang
                if (position == 1) { // update
                    keranjangRepository.updateKeranjang(uuid, item) {
                        if (!it) {
                            showToast("gagal update produk dalam keranjang")
                            finish()
                        }
                    }
                }

                if (position == 0) { // delete
                    keranjangRepository.removeKeranjang(uuid, item) {
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