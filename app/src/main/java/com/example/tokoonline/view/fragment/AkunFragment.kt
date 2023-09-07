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

    lateinit var s: SharedPref
    lateinit var btnLogout:Button

    private lateinit var binding: FragmentAkunBinding

    // TODO: Rename and change types of parameters

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAkunBinding.inflate(layoutInflater)

        binding.tvNama.text = userRepository.nama
//        binding.tvEmail.text = userRepository.
        // Inflate the layout for this fragment
//        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
//        btnLogout = view.findViewById(R.id.btn_logout)
//
//        s = SharedPref(requireActivity())
//
//        btnLogout.setOnClickListener {
//            s.setStatusLogin(false)
//        }

        return binding.root
    }
}