package com.example.tokoonline.data.repository

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.core.util.multiValueListenerFlow
import com.example.tokoonline.data.model.Alamat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow

class AlamatRepository {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constant.REFERENCE_ALAMAT)

    companion object {
        @Volatile
        private var INSTANCE: AlamatRepository? = null

        fun getInstance(): AlamatRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = AlamatRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    fun pushAlamat(alamat: Alamat, userUid: String, onComplete: (isSuccess: Boolean) -> Unit){
        val alamatRef = databaseReference.child(userUid).push()
        alamatRef.key?.let {
            val addedProduk = alamat.copy(id = it)
            alamatRef.setValue(addedProduk)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }
    fun getAlamat(userUid: String): Flow<List<Alamat?>> {
        return databaseReference.child(userUid).multiValueListenerFlow(Alamat::class.java)
    }

    fun getAlamatById(
        id: String,
        userUid: String,
        onComplete: (Alamat?) -> Unit
    ) {
        val userRef = databaseReference.child(userUid) // Reference to the specific user node

        userRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alamat = snapshot.children.firstOrNull()?.getValue(Alamat::class.java)
                onComplete(alamat)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }


    fun updateAlamat(
        userUid: String,
        alamat: Alamat,
        onComplete: (Boolean) -> Unit) {
        val alamatRef = databaseReference.child(userUid).child(alamat.id?:"")

        alamatRef.updateChildren(alamat.toMap())
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
    fun setDefaultAlamat(id: String, userUid: String, onComplete: (Boolean) -> Unit) {
        getAlamatByDefault(userUid) { currentDefaultAlamat ->
            if (currentDefaultAlamat != null) {
                clearDefaultFlag(currentDefaultAlamat, userUid) { isCleared ->
                    if (isCleared) {
                        toggleIsDefault(id, userUid) { isSuccessful ->
                            onComplete(isSuccessful)
                        }
                    } else {
                        onComplete(false) // Failed to clear the default flag
                    }
                }
            } else {
                toggleIsDefault(id, userUid) { isSuccessful ->
                    onComplete(isSuccessful)
                }
            }
        }
    }

    private fun toggleIsDefault(
        id: String,
        userUid: String,
        onComplete: (Boolean) -> Unit
    ) {
        val alamatRef = databaseReference.child(userUid).child(id)

        alamatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alamat = snapshot.getValue(Alamat::class.java)
                if (alamat != null) {
                    val newIsDefault = !alamat.default
                    alamatRef.child("default").setValue(newIsDefault)
                        .addOnCompleteListener { task ->
                            onComplete(task.isSuccessful)
                        }
                } else {
                    onComplete(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(false)
            }
        })
    }

    private fun clearDefaultFlag(
        alamat: Alamat,
        userUid: String,
        onComplete: (Boolean) -> Unit
    ) {
        alamat.default = false // Clear the default flag

        val alamatRef = databaseReference.child(userUid).child(alamat.id.toString())
        alamatRef.setValue(alamat)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    private fun setNewDefault(
        id: String,
        userUid: String,
        onComplete: (Boolean) -> Unit
    ) {
        val alamatRef = databaseReference.child(userUid).child(id)
        val updates = mapOf("default" to true)

        alamatRef.updateChildren(updates)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun getAlamatByDefault(
        userUid: String,
        onComplete: (Alamat?) -> Unit
    ) {
        val userRef = databaseReference.child(userUid)
        userRef.orderByChild("default").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alamat = snapshot.children.firstOrNull()?.getValue(Alamat::class.java)
                onComplete(alamat)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }

    fun updateAlamatDefault(
        userUid: String,
        idAlamat: String,
        isDefault: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        val alamatRef = databaseReference.child(userUid).child(idAlamat)

        val updates = mapOf("default" to isDefault)

        alamatRef.updateChildren(updates)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
    fun deleteAlamatById(id: String, onComplete: (Boolean) -> Unit) {
        databaseReference.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
    }
}