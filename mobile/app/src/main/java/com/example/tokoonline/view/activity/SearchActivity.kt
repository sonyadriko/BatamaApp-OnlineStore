package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.data.repository.ProdukRepository
import com.example.tokoonline.databinding.ActivitySearchBinding
import com.example.tokoonline.view.adapter.SearchResultAdapter


class SearchActivity : BaseActivity() {

    private val produkRepository : ProdukRepository by lazy {
        ProdukRepository.getInstance()
    }

    private lateinit var binding: ActivitySearchBinding
    private val adapter : SearchResultAdapter by lazy {
        SearchResultAdapter(object : OnItemClick {
            override fun onClick(data: Any, position: Int) {
                showToast((data as Produk).nama)
            }
        })
    }
    private var searchableKeyword = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {
        binding.rvSearchResult.adapter = adapter
    }

    private fun initListener() = with(binding) {
        btnBack.setOnClickListener {
            finish()
        }

        searchbar.addTextChangedListener {
            searchableKeyword = it.toString()
        }

        btnSearch.setOnClickListener {
            if (searchableKeyword.isEmpty()) return@setOnClickListener

            showProgressDialog()
            produkRepository.searchProduct(searchableKeyword) { isSuccess, data ->
                try {
                    if (isSuccess || data?.isNotEmpty() == true) {
                        adapter.submitData(data?.filterNotNull()!!)
                    } else throw Exception("data is null")
                } catch (e: Exception) {
                    showToast("Data produk tidak ditemukan")
                }
                dismissProgressDialog()
            }
        }
    }
}