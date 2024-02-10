package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tokoonline.BuildConfig
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.data.repository.midtrans.MidtransRepository
import com.example.tokoonline.databinding.ActivityPembayaranBinding
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PembayaranActivity : BaseActivity() {

    private var transaction: Transaction? = null
    companion object {
        private const val EXTRA_TRANSACTION = "snap_extra"
        fun createIntent(
            context: Context,
            transaction: Transaction,
        ): Intent {
            return Intent(context, PembayaranActivity::class.java).apply {
                putExtra(EXTRA_TRANSACTION, transaction)
            }
        }
    }

    @Inject
    lateinit var midtransRepository: MidtransRepository

    private val transactionRepository: TransactionRepository = TransactionRepository.getInstance()
    private lateinit var binding: ActivityPembayaranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        buildUiKit()
        showProgressDialog()
        initExtrasData()

        setContentView(binding.root)
    }

    private fun startTransaction(snap: String) {
        try {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                activity = this@PembayaranActivity,
                launcher = launcher,
                snapToken = snap,
            )
        } catch (e: Exception) {
            Timber.e(e)
            showToast(getString(R.string.something_wrong))
            startActivity(Intent(this@PembayaranActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    private fun initExtrasData() {
        transaction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(EXTRA_TRANSACTION, Transaction::class.java)
        } else {
            intent.extras?.getParcelable(EXTRA_TRANSACTION)
        }

        if (transaction == null) {
            dismissProgressDialog()
            showToast(getString(R.string.something_wrong))
            finish()
        } else {
            startTransaction(transaction!!.snapToken)
        }
    }

    private fun updateStatus(status: String) {
        showProgressDialog()

        transactionRepository.updateTransaction(transaction!!.copy(status = status)) { result ->
            if (result) {
                dismissProgressDialog()
                startActivity(Intent(this@PembayaranActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } else {
                dismissProgressDialog()
                showToast("gagal mengubah status transaksi, mohon hubungi support batama")
                finish()
            }
        }
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(BuildConfig.SERVER_URL)
            .withMerchantClientKey(BuildConfig.CLIENT_KEY)
            .enableLog(true)
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult =
                        it.parcelable<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)

                    if (transactionResult != null) {
                        when (transactionResult.status) {
                            UiKitConstants.STATUS_SUCCESS -> {
//                                Toast.makeText(
//                                    this,
//                                    "Transaction Finished. ID: " + transactionResult.transactionId,
//                                    Toast.LENGTH_LONG
//                                ).show()
                                updateStatus("success")
                            }

                            UiKitConstants.STATUS_PENDING -> {
                                dismissProgressDialog()
                                finish()
                            }
                            else -> {
                                updateStatus("canceled")
                            }
                        }
                    } else {
                        updateStatus("canceled")
                    }
                }
            }
        }
}