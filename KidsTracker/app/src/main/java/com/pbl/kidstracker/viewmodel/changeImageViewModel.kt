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

class changeImageViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _image = MutableLiveData("")
    val image: LiveData<String> = _image

    fun updateImage(image: String) {
        _image.value = image
    }

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    fun changeImage(userscreen: () -> Unit){
        val newPassword: String? = _image.value?.trim()

        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        /* code chang image*/


    }
}