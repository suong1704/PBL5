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

class TopBarViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name


    private val _phone = MutableLiveData("")
    val phone: LiveData<String> = _phone


    fun updateName(newName: String) {
        _name.value = newName
    }


    fun updatePhone(newPhone: String) {
        _phone.value = newPhone
    }

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status
    init {
        getInfo()
    }
    fun getInfo() {
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        val user =
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("Users").child(userid)
        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateName(snapshot!!.child("name").getValue().toString())
                updatePhone(snapshot!!.child("phone number").getValue().toString())

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("TOP BAR", "Failed to read value.", error.toException())
            }
        })
    }
    fun Logout(login: () -> Unit) {
        auth.signOut()
        login()
    }


}