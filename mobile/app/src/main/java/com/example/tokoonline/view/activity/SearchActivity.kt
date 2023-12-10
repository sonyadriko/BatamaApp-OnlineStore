package com.example.tokoonline.view.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.addTextChangedListener
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.databinding.ActivitySearchBinding
import com.example.tokoonline.view.adapter.SearchResultAdapter

class SearchActivity : BaseActivity() {

    private val produkRepository: ProdukRepository by lazy {
        ProdukRepository.getInstance()
    }

    private lateinit var binding: ActivitySearchBinding
    private val adapter: SearchResultAdapter by lazy {
        SearchResultAdapter(object : OnItemClick {
            override fun onClick(data: Any, position: Int) {
                startActivity(
                    DetailProductActivity.createIntent(
                        this@SearchActivity,
                        data as Produk
                    )
                )
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

        searchbar.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchProduct(searchableKeyword)
                return@OnEditorActionListener true
            }
            false
        })

        btnSearch.setOnClickListener {
            searchProduct(searchableKeyword)
        }
    }

    fun searchProduct(searchableKeyword: String) {
        if (searchableKeyword.isEmpty()) return

        showProgressDialog()
        produkRepository.searchProduct(searchableKeyword.lowercase()) { isSuccess, data ->
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