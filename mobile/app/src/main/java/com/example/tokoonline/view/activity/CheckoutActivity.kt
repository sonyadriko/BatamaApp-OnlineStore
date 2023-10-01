package com.example.tokoonline.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.convertLongToTime
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.api.model.BankTransferRequest
import com.midtrans.sdk.uikit.api.model.CreditCard
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.Expiry
import com.midtrans.sdk.uikit.api.model.GopayPaymentCallback
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.PaymentCallback
import com.midtrans.sdk.uikit.api.model.PaymentType
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import java.util.UUID


class CheckoutActivity : BaseActivity() {

    private companion object {
        const val CLIENT_KEY = "SB-Mid-client-hOWJXiCCDRvT0RGr"
        const val SERVER_URL = "https://tokoonline-payment-processor-8621e1ad3bef.herokuapp.com/"
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult =
                        it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    showToast("${transactionResult?.transactionId}", Toast.LENGTH_LONG)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val itemDetails = listOf(ItemDetails("Test01", 50000.00, 1, "lalalala"))

        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(SERVER_URL)
            .withMerchantClientKey(CLIENT_KEY)
            .enableLog(true)
            .build()

        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this, // activity
            launcher, //ActivityResultLauncher
            SnapTransactionDetail(
                UUID.randomUUID().toString(),
                50000.00,
                "IDR"
            ), // Transaction Details
            CustomerDetails(
                "budi-6789",
                "Budi",
                "Utomo",
                "budi@utomo.com",
                "0213213123",
                null,
                null
            ), // Customer Details
            itemDetails, // Item Details
            CreditCard(
                false,
                null,
                "",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
            ), // Credit Card
            "customerIdentifier", // User Id
            PaymentCallback("mysamplesdk://midtrans"), // UobEzpayCallback
            GopayPaymentCallback("mysamplesdk://midtrans"), // GopayCallback
            PaymentCallback("mysamplesdk://midtrans"), // ShopeepayCallback
            Expiry(
                convertLongToTime(System.currentTimeMillis()),
                Expiry.UNIT_HOUR,
                5
            ), // expiry (null: default expiry time)
            PaymentMethod.CREDIT_CARD, // Direct Payment Method Type
            listOf(
                PaymentType.CREDIT_CARD,
                PaymentType.GOPAY,
                PaymentType.SHOPEEPAY,
                PaymentType.UOB_EZPAY
            ), // Enabled Payment (null: enabled all available payment)
            BankTransferRequest(vaNumber = "1234567890"), // Permata Custom VA (null: default va)
            BankTransferRequest(vaNumber = "12345"), // BCA Custom VA (null: default va)
            BankTransferRequest(vaNumber = "12345"), // BNI Custom VA (null: default va)
            BankTransferRequest(vaNumber = "12345"), // BRI Custom VA (null: default va)
            "Cash1", // Custom Field 1
            "Debit2", // Custom Field 2
            "Credit3"  // Custom Field 3
        )
    }

    private var customerDetails = CustomerDetails(
        firstName = "user fullname",
        customerIdentifier = "mail@mail.com",
        email = "mail@mail.com",
        phone = "085310102020"
    )
    private var itemDetails = listOf(ItemDetails("test-01", 36500.0, 1, "test01"))

    private fun initTransactionDetails(): SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = 36500.0
        )
    }

    private fun startTransactionFlow() {
        SdkUIFlowBuilder.init()
            .setClientKey(CLIENT_KEY)
            .setMerchantBaseUrl(SERVER_URL)
            .setLanguage("id")
            .setTransactionFinishedCallback { transactionResult ->
                doOnTransactionFinished(transactionResult)
            }
            .enableLog(false) // this is to disable logging
            .buildSDK()

        MidtransSDK.getInstance().startPaymentUiFlow(this)
    }

    private fun doOnTransactionFinished(transactionResult: com.midtrans.sdk.corekit.models.snap.TransactionResult?) {
        if (transactionResult != null) {
            when (transactionResult.status) {
                STATUS_SUCCESS -> {
                    showToast(
                        "Transaction Finished. ID: " + transactionResult.response.transactionId,
                        Toast.LENGTH_LONG
                    )
                }

                STATUS_PENDING -> {
                    showToast(
                        "Transaction Pending. ID: " + transactionResult.response.transactionId,
                        Toast.LENGTH_LONG
                    )
                }

                STATUS_FAILED -> {
                    showToast(
                        "Transaction Failed. ID: " + transactionResult.response.transactionId,
                        Toast.LENGTH_LONG
                    )
                }

                STATUS_INVALID -> {
                    showToast(
                        "Transaction Invalid. ID: " + transactionResult.response.transactionId,
                        Toast.LENGTH_LONG
                    )
                }

                else -> {
                    showToast(
                        "Transaction ID: " + transactionResult.response.transactionId + ". Message: " + transactionResult.status,
                        Toast.LENGTH_LONG
                    )
                }
            }
        } else {
            showToast("Transaction Invalid", Toast.LENGTH_LONG)
        }
    }
}