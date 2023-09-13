package com.example.tokoonline.core.util

import com.example.tokoonline.data.model.Produk

interface OnItemClickListener {
    fun onItemClick(data: Produk, position: Int)
}