package com.example.tokoonline.data.model.midtrans

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SnapTransactionDetailRequest(
	@SerializedName("customer_details")
	val customerDetails: CustomerDetails,
	@SerializedName("item_details")
	val itemDetails: List<ItemDetailsItem>,
	@SerializedName("transaction_details")
	val transactionDetails: TransactionDetails
) : Parcelable

@Parcelize
data class TransactionDetails(
	@SerializedName("gross_amount")
	val grossAmount: Double,
	@SerializedName("order_id")
	val orderId: String
) : Parcelable

@Parcelize
data class ShippingAddress(
	@SerializedName("country_code")
	val countryCode: String,
	@SerializedName("address")
	val address: String,
	@SerializedName("phone")
	val phone: String,
	@SerializedName("city")
	val city: String,
	@SerializedName("last_name")
	val lastName: String,
	@SerializedName("postal_code")
	val postalCode: String,
	@SerializedName("first_name")
	val firstName: String,
	@SerializedName("email")
	val email: String
) : Parcelable

@Parcelize
data class BillingAddress(
	@SerializedName("country_code")
	val countryCode: String,
	@SerializedName("address")
	val address: String,
	@SerializedName("phone")
	val phone: String,
	@SerializedName("city")
	val city: String,
	@SerializedName("last_name")
	val lastName: String,
	@SerializedName("postal_code")
	val postalCode: String,
	@SerializedName("first_name")
	val firstName: String,
	@SerializedName("email")
	val email: String
) : Parcelable

@Parcelize
data class CustomerDetails(
	@SerializedName("phone")
	val phone: String,
	@SerializedName("last_name")
	val lastName: String,
	@SerializedName("billing_address")
	val billingAddress: BillingAddress,
	@SerializedName("shipping_address")
	val shippingAddress: ShippingAddress,
	@SerializedName("first_name")
	val firstName: String,
	@SerializedName("email")
	val email: String
) : Parcelable

@Parcelize
data class ItemDetailsItem(
	@SerializedName("quantity")
	val quantity: Int,
	@SerializedName("price")
	val price: Double,
	@SerializedName("name")
	val name: String,
	@SerializedName("merchant_name")
	val merchantName: String,
	@SerializedName("id")
	val id: String,
	@SerializedName("category")
	val category: String,
	@SerializedName("brand")
	val brand: String,
	@SerializedName("url")
	val url: String
) : Parcelable
