package com.pbl.kidstracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Error

class RegisterViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _phone = MutableLiveData("")
    val phone: LiveData<String> = _phone

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _device = MutableLiveData("")
    val device: LiveData<String> = _device

    fun updateDevice(newDivice: String) {
        _device.value = newDivice
    }
    private val _image = MutableLiveData("")
    val image: LiveData<String> = _image

    fun updateImage(image: String) {
        _image.value = image
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePhone(newPhone: String) {
        _phone.value = newPhone
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    fun register(maincontent: () ->  Unit){
        val name: String? = _name.value?.trim()
        val email: String? =_email.value?.trim()
        val phone: String? =_phone.value?.trim()
        val password: String? = _password.value?.trim()
        val device: String? = _device.value?.trim()

        val devicename = FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
            .getReference("device")
        devicename.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                for (child in dataSnapshot.children) {
                    if (child.key.toString().equals(device)){
                        if (email != null && password != null && name != null && phone != null && device != null) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener{
                                    if (it.isSuccessful) {
                                        val firebaseUser = auth.currentUser!!
                                        val userid = firebaseUser!!.uid
                                        val user =FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                                            .getReference("Users").child(userid)
                                        val hashMap = HashMap<String, String>()
                                        hashMap["email"] = email
                                        hashMap["name"] = name
                                        hashMap["password"] = password
                                        hashMap["phone number"] = phone
                                        hashMap["age"] = ""
                                        hashMap["gender"] = "Other"
                                        hashMap["device"] = device
                                        hashMap["image"] = "https://firebasestorage.googleapis.com/v0/b/esp8266-72053.appspot.com/o/images%2Fdefault.png?alt=media&token=cc31cc70-decc-448f-b814-9d38f3480a07"
                                        user.setValue(hashMap)
                                            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                                                if (task.isSuccessful) {
                                                    maincontent()
                                                } else{
                                                    Log.d("error", "failed")
                                                    _status.postValue(task.exception!!.message)
                                                }
                                            })
                                    }else {
                                        _status.postValue("Register failed!")
                                    }
                                }
                        }
                    }else{
                        _status.postValue("The device has existed!")
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}