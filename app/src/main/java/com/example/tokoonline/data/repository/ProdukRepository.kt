package com.example.tokoonline.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.core.util.toProdukDomain
import com.example.tokoonline.data.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ProdukRepository {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_PRODUK)

    companion object {
        @Volatile
        private var INSTANCE: ProdukRepository? = null

        fun getInstance(): ProdukRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = ProdukRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    fun loadProduk(): Flow<List<Produk?>> {
        println("FETCH_PRODUK")
        return databaseReference.multiValueListenerFlow(Produk::class.java)
    }

    fun addProduk(produk: Produk, onComplete: (isSuccess: Boolean) -> Unit) {
        val produkRef = databaseReference.push()
        produkRef.setValue(produk)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }

//        databaseReference.child(produk.nama).setValue(produk)
//            .addOnCompleteListener {
//                onComplete(it.isSuccessful)
//            }
    }

    fun updateProduk(produk: Produk, onComplete: (isSuccess: Boolean) -> Unit) {
        databaseReference.child(produk.nama)
            .updateChildren(produk.toMap())
            .addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }

    fun getProdukDetail(
        namaProduk: String,
        onComplete: (data: Produk) -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        databaseReference.child(namaProduk)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        onComplete(snapshot.toProdukDomain())
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.toException())
                }
            })
    }

    fun removeProduk(namaProduk: String, onComplete: (isSuccess: Boolean) -> Unit) {
        databaseReference.child(namaProduk).removeValue { error, _ ->
            onComplete(error == null)
        }
    }

}