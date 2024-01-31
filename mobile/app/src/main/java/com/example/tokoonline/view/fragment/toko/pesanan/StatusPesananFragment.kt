package com.example.tokoonline.view.fragment.toko.pesanan

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.R
import com.example.tokoonline.databinding.FragmentStatusPesananBinding
import com.example.tokoonline.view.activity.toko.pesanan.PageViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class StatusPesananFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentStatusPesananBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatusPesananBinding.inflate(inflater, container, false)
        val root = binding.root

        initView()
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(_binding!!) {
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> {
                    cover.setImageResource(R.drawable.delivery)
                    subtitle.text = "Pesanan yang perlu dikirim akan ditampilkan\npada halaman ini"
                }

                1 -> {
                    cover.setImageResource(R.drawable.cancel)
                    subtitle.text = "Pesanan yang telah dibatalkan akan ditampilkan\npada halaman ini"
                }

                2 -> {
                    cover.setImageResource(R.drawable.selesai)
                    subtitle.text = "Pesanan yang telah selesai akan ditampilkan\npada halaman ini"
                }
            }
        })
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): StatusPesananFragment {
            return StatusPesananFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}