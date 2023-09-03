package com.example.tokoonline

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityTambahProdukBinding

class TambahProduk : BaseActivity() {

    private lateinit var binding: ActivityTambahProdukBinding
//    private val viewModel: TambahProdukViewModel by viewModels()
//    private lateinit var storageReference: StorageReference
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_produk)
    }
}