package com.pbl.kidstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    fun updateStatus(newStatus: String){
        _status.value = newStatus
    }



     fun login(maincontent: () ->  Unit){
         val email: String? = _email.value?.trim()
         val password: String? = _password.value?.trim()
         if (email != null && password != null) {
             auth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener{
                     if (it.isSuccessful){
                         maincontent()
                     } else{
                         _status.postValue("Login failed")
                     }
                 }
         }
         }


    fun createRegister(register: () ->  Unit){
        register()
    }
}