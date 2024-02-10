package com.example.tokoonline.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.util.SharedPref
import com.example.tokoonline.databinding.FragmentAkunBinding
import timber.log.Timber
import java.net.URLEncoder


/**
 * A simple [Fragment] subclass.
 */

class AkunFragment : BaseFragment() {



    private lateinit var binding: FragmentAkunBinding

    // TODO: Rename and change types of parameters

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAkunBinding.inflate(layoutInflater)

        binding.tvNama.text = userRepository.nama
        binding.tvEmail.text = userRepository.email

        binding.btnLogout.setOnClickListener {
           logout()
        }

        binding.btnEditProfile.setOnClickListener{
         goToEditProfil()
        }

        binding.btnKeranjang.setOnClickListener{
            goToKeranjang()
        }

        binding.btnSettingAlamat.setOnClickListener{
            goToSettingAlamat()
        }

        binding.btnCallAdmin.setOnClickListener{
            openWhatsApp("Halo admin TokoOnline Batama")
        }

        binding.btnProfileToko.setOnClickListener{
            goToTokoProfile()
        }

        binding.btnEditAkun.setOnClickListener{
            goToEditAkun()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        refreshData()
    }

    private fun refreshData() {
        binding.tvNama.text = userRepository.nama
        binding.tvEmail.text = userRepository.email
    }

    private fun openWhatsApp(pesan: String? = null) {
        try {
            val nomor = "085850319392"
            val newNomor = if (nomor[0].equals('0', true)) {
                nomor.replaceFirst("0", "+62")
            } else nomor
            val i = Intent(Intent.ACTION_VIEW)
            var url = "https://api.whatsapp.com/send?phone=$newNomor"
            if (pesan != null) {
                val holder = url
                url = holder + "&text=" + URLEncoder.encode(pesan, "UTF-8")
            }

            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)

            startActivity(i)
        } catch (e: Exception) {
            Timber.tag("ERROR WHATSAPP").e(e.toString())
            showToast("Tidak ada WhatApp")
        }
    }

}