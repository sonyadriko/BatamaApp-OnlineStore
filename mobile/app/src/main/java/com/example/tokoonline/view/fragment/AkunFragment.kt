package com.example.tokoonline.view.fragment

import android.annotation.SuppressLint
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
//            binding.tvPhone.text = userRepository.phone

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

        binding.btnProfileToko.setOnClickListener{
            goToTokoProfile()
        }

        binding.btnEditAkun.setOnClickListener{
            goToEditAkun()
        }


        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//
//        // Refresh data or perform actions when the fragment is resumed
//        refreshData()
//    }
//
//    private fun refreshData() {
//        // You can put the code here to refresh data or perform any actions needed
//        // For example, if you want to refresh the user information, you can do something like this:
//        binding.tvNama.text = userRepository.nama
//        binding.tvEmail.text = userRepository.idToko
//    }
}