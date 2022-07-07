package com.pbl.kidstracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class LoadImageViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _image = MutableLiveData("")
    val image: LiveData<String> = _image

    fun updateImage(image: String) {
        _image.value = image
    }

    init {
        getImageLink()
    }

    fun getImageLink(){
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        val user =
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("Users").child(userid)
        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateImage(snapshot!!.child("image").getValue().toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("UPDATE", "Failed to read value.", error.toException())
            }
        })

    }
}