package com.example.tokoonline.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.toProdukDomain
import com.example.tokoonline.data.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProdukRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_PRODUK)

    @Volatile
    private var INSTANCE: ProdukRepository? = null

    fun getInstance(): ProdukRepository {
        return INSTANCE ?: synchronized(this) {

            val instance = ProdukRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadProduk(): LiveData<List<Produk>> {
        val data = MutableLiveData<List<Produk>>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    data.value = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Produk::class.java)!!
                    }
                } catch (e: Exception) {
                    data.value = emptyList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                data.value = emptyList()
            }
        })

        return data
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