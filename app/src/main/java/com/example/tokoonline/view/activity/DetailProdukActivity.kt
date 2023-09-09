package com.example.tokoonline.view.activity

import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityDetailProdukBinding
import com.example.tokoonline.view.viewmodel.DetailViewModel

class DetailProdukActivity : BaseActivity(){

    private lateinit var binding: ActivityDetailProdukBinding
    private lateinit var namaproduk: String

    private val viewModel: DetailViewModel by viewModel()
    companion object {
        const val RESULT_DELETE = 10
    }
}