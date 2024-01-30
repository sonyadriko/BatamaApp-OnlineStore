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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukSayaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProdukViewModel::class.java)


        val tokoID = intent.getStringExtra("tokoID").toString()
        showProgressDialog()
        loadProdukbyTokoID(tokoID)

    }

    fun loadProdukbyTokoID(tokoID: String) {
        viewModel.loadProdukbyIDToko(tokoID) { produkList ->
            dismissProgressDialog()
            val recyclerView: RecyclerView = binding.rvProduksaya
            val adapter = AdapterItemProdukSaya(produkList)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

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

}