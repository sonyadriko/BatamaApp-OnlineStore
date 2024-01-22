package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivitySettingAlamat1Binding
import com.example.tokoonline.view.adapter.AlamatAdapter
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import kotlinx.coroutines.launch


class SettingAlamatActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingAlamat1Binding
    private lateinit var viewModel : AlamatViewModel
    private var uuid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAlamat1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AlamatViewModel::class.java)

        lifecycleScope.launch {
            userRepository.uid?.let {
                uuid = it
                getAlamat(uuid)
            }
        }

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

//        binding.btnTambahAlamat.setOnClickListener {
//            goToAlamatForm()
//        }



    }

    private fun getAlamat(userUid: String) {
        viewModel.getAlamat(userUid) { alamatList ->

            val recyclerView: RecyclerView = binding.rvAlamat

            val adapter = AlamatAdapter(alamatList)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            adapter.onCardViewClickListener = {alamat ->
                val id = alamat.id.toString()
                viewModel.setDefaultAlamat(id, userUid) { isSuccessful ->
                    if (isSuccessful) {
                        showToast("Alamat default berhasil di update")
                    } else {
                        // Failed to set the default address
                        // Handle the error or display a message to the user
                    }
                }


            }

            adapter.onUbahAlamatClickListener = { alamat ->
                goToAlamatForm(selectedAlamatId = alamat.id)
            }
        }
    }
}
