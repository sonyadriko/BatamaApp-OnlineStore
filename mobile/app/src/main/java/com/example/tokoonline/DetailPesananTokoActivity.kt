package com.example.tokoonline

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.databinding.ActivityDetailPesananTokoBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel

class DetailPesananTokoActivity : BaseActivity() {
    private lateinit var binding : ActivityDetailPesananTokoBinding
    private lateinit var viewModelAlamat : AlamatViewModel
    private lateinit var viewModelToko : TokoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)
        viewModelToko = ViewModelProvider(this).get(TokoViewModel::class.java)

        val userId = userRepository.uid.toString()


        val data = intent.getSerializableExtra("data") as? Transaction
        if (data != null){
            if(data.status.equals("pending",true)){
                binding.statusCancel.gone()
                binding.statusSuccess.gone()
            }
            if(data.status.equals("canceled", true)){
                binding.statusPending.gone()
                binding.statusSuccess.gone()
            }
            if(data.status.equals("success", true)){
                binding.statusPending.gone()
                binding.statusCancel.gone()
            }
        }
        showAlamatDefault(userId)

    }

    @SuppressLint("SetTextI18n")
    private fun showAlamatDefault(userId: String) {
        viewModelAlamat.getAlamatDefault(userId) { alamatDef ->
            with(binding) {
                if (alamatDef != null) {
                    alamatDefault.visible()

                    alamatDefault.text =
                        "${alamatDef.nama} \u2022 ${alamatDef.phone}\n${alamatDef.alamat}"
                } else {

                }
            }
        }
    }
}