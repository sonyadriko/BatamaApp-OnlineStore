package com.example.tokoonline.view.activity

import android.os.Bundle
import android.text.Editable
import android.view.View
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityEditProfilFormBinding

class EditProfilFormActivity : BaseActivity(){
    private lateinit var binding: ActivityEditProfilFormBinding


    fun String.toEditable() : Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cardToShow = intent.getStringExtra("cardToShow")

        if (cardToShow != null) {
            // Hide all cards
            // Show the card associated with the button clicked
            when (cardToShow) {
                "formNama" -> {
                    binding.formNama.visibility = View.VISIBLE
                    binding.formPhone.visibility = View.GONE
                    // Add more cases for other cards if needed
                }

                "formPhone" -> {
                    binding.formPhone.visibility = View.VISIBLE
                    binding.formNama.visibility = View.GONE
                    // Add more cases for other cards if needed
                }
                // Add more cases for other cards if needed
            }
        }


        binding.tvNama.text = userRepository.nama!!.toEditable()
        binding.tvPhone.text = userRepository.phone!!.toEditable()




    }
}