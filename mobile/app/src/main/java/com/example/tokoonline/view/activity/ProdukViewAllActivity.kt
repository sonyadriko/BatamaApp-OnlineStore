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
        if (packageName == "com.example.tokoonline.view.fragment") {
            productData = intent.getParcelableArrayListExtra<Produk>("productTerlarisData").orEmpty()
            binding.tvTitle.text = "Produk Terlaris"
        } else {
            productData = intent.getParcelableArrayListExtra<Produk>("productDitawarkanData").orEmpty()
            binding.tvTitle.text = "Produk Ditawarkan"
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
