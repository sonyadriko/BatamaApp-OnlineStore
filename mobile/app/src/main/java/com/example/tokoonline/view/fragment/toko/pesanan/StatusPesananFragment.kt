package com.example.tokoonline.view.fragment.toko.pesanan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.DetailPesananTokoActivity
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseFragment
import com.example.tokoonline.core.util.OnItemClick
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.databinding.FragmentStatusPesananBinding
import com.example.tokoonline.view.activity.PembayaranActivity
import com.example.tokoonline.view.activity.toko.pesanan.PageViewModel
import com.example.tokoonline.view.adapter.AdapterRiwayat
import com.example.tokoonline.view.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch

/**
 * A placeholder fragment containing a simple view.
 */
class StatusPesananFragment : BaseFragment() {
    private var uuid = ""
    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentStatusPesananBinding? = null
    private lateinit var viewModel: TransactionViewModel  // tambahkan inisialisasi ini


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
        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        lifecycleScope.launch {
            userRepository.uid?.let {
                uuid = it
//                getRiwayat(uuid)
                getRiwayat(uuid, pageViewModel.text.value ?: 0)

            }
        }



        initView()
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(_binding!!) {
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> getRiwayat(uuid, 0)
                1 -> getRiwayat(uuid, 1)
                2 -> getRiwayat(uuid, 2)
            }

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

    private fun getRiwayat(userUid: String, statusPesanan: Int) {
        showProgressDialog()
        viewModel.getTransaction(userUid) { transactionList ->
            dismissProgressDialog()

            val filteredTransactionList = when (statusPesanan) {
                0 -> transactionList.filter { it.status.equals("pending", ignoreCase = true) }
                1 -> transactionList.filter { it.status.equals("canceled", ignoreCase = true) }
                2 -> transactionList.filter { it.status.equals("success", ignoreCase = true) }
                else -> transactionList
            }

            val recyclerView: RecyclerView = binding.rvRiwayat
            val adapter = AdapterRiwayat(filteredTransactionList, object : OnItemClick {
                override fun onClick(data: Any, position: Int) {
                    if ((data as Transaction).status.equals("pending", ignoreCase = true)
                        && data.metodePembayaran.equals("cod", ignoreCase = true).not()
                    ) {
                        startActivity(
                            PembayaranActivity.createIntent(
                                requireActivity(),
                                data
                            )
                        )
                    } else {
                        val intent = Intent(requireActivity(), DetailPesananTokoActivity::class.java)
                        intent.putExtra("data", data)
                        startActivity(intent)
                    }
                }
            })

            if (filteredTransactionList.isNotEmpty()) {
                binding.placeholder.gone()
                binding.rvRiwayat.visible()
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            } else {
                binding.placeholder.visible()
                binding.rvRiwayat.gone()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}