package com.example.tokoonline.view.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.databinding.ActivityProdukViewAllBinding
import com.example.tokoonline.view.adapter.AdapterProdukAll

class ProdukViewAllActivity : BaseActivity(), OnItemClick {

    private lateinit var binding: ActivityProdukViewAllBinding
    private lateinit var productAdapterAll: AdapterProdukAll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productData: List<Produk>
        val intentName = intent.getStringExtra("intentName") // Get the intent name

        when (intentName) {
            "productDitawarkanData" -> {
                productData = intent.getParcelableArrayListExtra<Produk>("productDitawarkanData").orEmpty()
                binding.tvTitle.text = "Produk Ditawarkan"
            }
            "productTerlarisData" -> {
                productData = intent.getParcelableArrayListExtra<Produk>("productTerlarisData").orEmpty()
                binding.tvTitle.text = "Produk Terlaris"
            }
            else -> {
                // Handle unknown intent names here or set a default behavior
                productData = emptyList()
                binding.tvTitle.text = "Unknown Category"
            }
        }


        productAdapterAll = AdapterProdukAll(this)

        val recyclerView: RecyclerView = binding.rvProdukAll

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = productAdapterAll

        productAdapterAll.submitList(productData)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onClick(data: Any, position: Int) {
        startActivity(
            DetailProductActivity.createIntent(
                this@ProdukViewAllActivity,
                data as Produk
            )
        )
    }
}
