package com.example.tokoonline.view.activity

import android.os.Bundle
import android.widget.Toast
import com.example.tokoonline.BuildConfig
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.getFormattedTimeMidtrans
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ExpiryModel
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.uikit.api.model.Expiry


class CheckoutActivity : BaseActivity() {

    private companion object {
        const val CLIENT_KEY = BuildConfig.CLIENT_KEY
        const val SERVER_URL = BuildConfig.SERVER_URL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTransactionFlow()
        finish()
    }

    private var itemDetailList = arrayListOf(ItemDetails("test-01", 36500.0, 1, "test01"))

    private fun initTransactionRequest(): TransactionRequest {
        // Create new Transaction Request
        val transactionRequestNew = TransactionRequest(System.currentTimeMillis().toString() + "", 36500.0)
        transactionRequestNew.expiry = ExpiryModel().apply {
            startTime = getFormattedTimeMidtrans(System.currentTimeMillis())
            duration = 5
            unit = Expiry.UNIT_MINUTE
        }
        transactionRequestNew.customerDetails = initCustomerDetails()
        transactionRequestNew.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequestNew.shopeepay = Shopeepay("mysamplesdk:://midtrans")

        transactionRequestNew.itemDetails = itemDetailList
        return transactionRequestNew
    }

    private fun initCustomerDetails(): CustomerDetails {
        return CustomerDetails().apply {
            firstName = "user fullname"
            customerIdentifier = "mail@mail.com"
            email = "mail@mail.com"
            phone = "085310102020"
        }
    }

    private fun startTransactionFlow() {
        SdkUIFlowBuilder.init()
            .setContext(this)
            .setClientKey(CLIENT_KEY)
            .setMerchantBaseUrl(SERVER_URL)
            .setLanguage("id")
            .setTransactionFinishedCallback { transactionResult ->
                doOnTransactionFinished(transactionResult)
            }
            .enableLog(true) // this is to disable logging
            .buildSDK()

        MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
        MidtransSDK.getInstance().startPaymentUiFlow(this@CheckoutActivity)
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