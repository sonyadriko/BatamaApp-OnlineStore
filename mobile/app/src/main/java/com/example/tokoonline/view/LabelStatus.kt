package com.example.tokoonline.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.tokoonline.R
import com.example.tokoonline.databinding.LabelStatusBinding

class LabelStatus constructor(context: Context, attributeSet: AttributeSet) :
    CardView(context, attributeSet) {

    val binding: LabelStatusBinding

    init {
        binding = LabelStatusBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setStatus(status: String) = with(binding) {
        if (status.equals("pending", true)) {
            content.text = "MENUNGGU"
            content.setTextColor(ContextCompat.getColor(
                context,
                R.color.colorPendingText
            ))
            root.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPendingBackground
                )
            )
            root.strokeColor = ContextCompat.getColor(
                context,
                R.color.colorPendingText
            )
        }

        if (status.equals("canceled", true)) {
            content.text = "DIBATALKAN"
            content.setTextColor(ContextCompat.getColor(
                context,
                R.color.colorCancelText
            ))
            root.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorCancelBackground
                )
            )
            root.strokeColor = ContextCompat.getColor(
                context,
                R.color.colorCancelText
            )
        }

        if (status.equals("success", true)) {
            content.text = "SELESAI"
            content.setTextColor(ContextCompat.getColor(
                context,
                R.color.colorSuccessText
            ))
            root.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSuccessBackground
                )
            )
            root.strokeColor = ContextCompat.getColor(
                context,
                R.color.colorSuccessText
            )
        }
    }
}