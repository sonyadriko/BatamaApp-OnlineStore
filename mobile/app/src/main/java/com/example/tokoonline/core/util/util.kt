package com.example.tokoonline.core.util

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.data.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}

fun getFormattedTimeMidtrans(time: Long): String {
    // Quoted "Z" to indicate UTC, no timezone offset
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
    df.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
    return df.format(Date(time))
}

fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    df.timeZone = TimeZone.getTimeZone("UTC")
    return df.parse(date)?.time ?: 0
}

fun convertStringToCalendar(date: String): Calendar {
    val fromDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    fromDate.timeZone = TimeZone.getTimeZone("UTC")
    val date = fromDate.parse(date)
    val cal = Calendar.getInstance(Locale.getDefault())
    cal.time = date
    return cal
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun String.toRole(): Constant.Role? {
    return Constant.Role.values().find {
        it.name.lowercase() == this
    }
}

fun DataSnapshot.toProdukDomain(): Produk {
    return getValue(Produk::class.java)!!
}

fun moneyFormatter(value: Long, withPrefix: Boolean = true): String {
    val myFormatter = DecimalFormat("#,###.###")
    val formatted = myFormatter.format(value.toDouble()).replace(",".toRegex(), ".")
    return if (withPrefix) "Rp$formatted" else formatted
}

/**
 * new way to retrieve parcelable data
 * because getParcelableExtra is deprecated
 */
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun <T> DatabaseReference.singleValueListenerFlow(dataType: Class<T>): Flow<T?> = callbackFlow {
    val listener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(dataType)
            trySend(value)
        }

        override fun onCancelled(error: DatabaseError) {
            cancel()
        }
    }
    addValueEventListener(listener)
    awaitClose { removeEventListener(listener) }
}.flowOn(Dispatchers.IO)

fun <T> DatabaseReference.multiValueListenerFlow(dataType: Class<T>): Flow<List<T?>> =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.children.map { snapshot ->
                    snapshot.getValue(dataType)
                }
                println("FETCH_PRODUK = DATA CHANGE $value")
                trySend(value)
            }

            override fun onCancelled(error: DatabaseError) {
                println("FETCH_PRODUK = CANCELLED")
                cancel()
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }