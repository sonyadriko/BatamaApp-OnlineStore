package com.example.tokoonline.data.repository

import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.data.model.Alamat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

    fun pushAlamat(alamat: Alamat, onComplete: (isSuccess: Boolean) -> Unit){
        val alamatRef = databaseReference.push()

        val idKey = alamatRef.key

        alamatRef.setValue(alamat.copy(id = idKey))
            .addOnCompleteListener{task ->
                onComplete(task.isSuccessful)
            }
    }
    fun getAlamatList(userRepository: UserRepository, onComplete: (List<Alamat>) -> Unit) {
        val query = databaseReference.orderByChild("id_users").equalTo(userRepository.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val alamatList = mutableListOf<Alamat>()
                for (snapshot in dataSnapshot.children) {
                    val alamat = snapshot.getValue(Alamat::class.java)
                    alamat?.let {
                        alamatList.add(it)
                    }
                }
                onComplete(alamatList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error here if needed
                onComplete(emptyList())
            }
        })
    }

    fun getAlamatById(id: String, onComplete: (Alamat?) -> Unit) {
        databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alamat = snapshot.getValue(Alamat::class.java)
                onComplete(alamat)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }

    fun updateAlamat(alamat: Alamat, onComplete: (Boolean) -> Unit) {

        val addressKey = alamat.id

        if (addressKey != null) {
            val addressUpdateData = mapOf(
                "/$addressKey" to alamat.toMap()
            )

            databaseReference.updateChildren(addressUpdateData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true)
                    } else {
                        onComplete(false)
                    }
                }
        } else {
            onComplete(false)
        }
    }
    fun deleteAlamatById(id: String, onComplete: (Boolean) -> Unit) {
        databaseReference.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true) // Deletion was successful
                } else {
                    onComplete(false) // Deletion failed
                }
            }
    }
}