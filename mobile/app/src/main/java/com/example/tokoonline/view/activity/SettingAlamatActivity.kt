package com.example.tokoonline.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.Alamat
import com.example.tokoonline.data.repository.UserRepository
import com.example.tokoonline.databinding.ActivitySettingAlamat1Binding
import com.example.tokoonline.databinding.ActivitySettingAlamatBinding
import com.example.tokoonline.view.adapter.AlamatAdapter
import com.example.tokoonline.view.viewmodel.AlamatViewModel


class SettingAlamatActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingAlamat1Binding
    private lateinit var viewModel : AlamatViewModel
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAlamat1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AlamatViewModel::class.java)

        recyclerView = binding.recyclerView

        fetchAlamatData()
//        binding.btnUbahAlamat.setOnClickListener{
//            goToAlamatForm()
//        }
        binding.btnTambahAlamat.setOnClickListener {
            goToAlamatForm()
        }

    }

    private fun fetchAlamatData() {
        val userRepository = userRepository // Replace with your actual UserRepository instantiation

        viewModel.fetchAlamatList(userRepository) { alamatList ->
            val adapter = AlamatAdapter(alamatList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

        }
    }


}
