package com.example.tokoonline.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityProdukSayaBinding
import com.example.tokoonline.view.adapter.AdapterItemProdukSaya
import com.example.tokoonline.view.viewmodel.ProdukViewModel


class ProdukSayaActivity : BaseActivity() {
    private lateinit var binding : ActivityProdukSayaBinding
    private lateinit var viewModel : ProdukViewModel

    private val tokoID: String by lazy {
        intent.getStringExtra("tokoID").toString()
    }

    private val adapter: AdapterItemProdukSaya by lazy {
        AdapterItemProdukSaya(this@ProdukSayaActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukSayaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProdukViewModel::class.java)


        val recyclerView: RecyclerView = binding.rvProduksaya
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        getData()
    }

    private fun getData() {
        showProgressDialog()
        loadProdukbyTokoID(tokoID)
    }

    private fun loadProdukbyTokoID(tokoID: String) {
        viewModel.loadProdukbyIDToko(tokoID) { produkList ->
            dismissProgressDialog()
            adapter.submitList(produkList)

            // Check if the produkList is empty
            if (produkList.isEmpty()) {
                // No produk, show produk_null layout
                binding.produkNull.visibility = View.VISIBLE
                binding.viewOnProduk.visibility = View.GONE
                binding.toolbarNull.binding.leftIcon.setOnClickListener {
                    finish()
                }
                binding.btnTambahProdukNull.setOnClickListener{

                    startActivity(Intent(this, TambahProdukActivity::class.java))
                }
            } else {
                // Produk exists, show view_on_produk layout
                binding.produkNull.visibility = View.GONE
                binding.viewOnProduk.visibility = View.VISIBLE
                binding.toolbar.binding.leftIcon.setOnClickListener {
                    finish()
                }
                binding.btnTambahProduk.setOnClickListener{
                    startActivity(Intent(this, TambahProdukActivity::class.java))
                }
            }
        }
    }
    fun deleteProduk(produkId: String, tokoID: String) {
        viewModel.deleteProdukById(produkId) { isSuccess ->
            if (isSuccess) {
               showToast("Success to delete product")
                loadProdukbyTokoID(tokoID)
            } else {
                showToast("Failed to delete product")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}