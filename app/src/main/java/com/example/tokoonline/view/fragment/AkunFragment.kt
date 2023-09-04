package com.example.tokoonline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tokoonline.R
import com.example.tokoonline.core.util.SharedPref


/**
 * A simple [Fragment] subclass.
 */

class AkunFragment : Fragment() {

    lateinit var s: SharedPref
    lateinit var btnLogout:Button

    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
//        btnLogout = view.findViewById(R.id.btn_logout)
//
//        s = SharedPref(requireActivity())
//
//        btnLogout.setOnClickListener {
//            s.setStatusLogin(false)
//        }

        return view
    }
}