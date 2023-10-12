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
        alamatRef.setValue(alamat)
            .addOnCompleteListener{task ->
                onComplete(task.isSuccessful)
            }
    }
    // Function to retrieve Alamat data that matches the userRepository.uid
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

}