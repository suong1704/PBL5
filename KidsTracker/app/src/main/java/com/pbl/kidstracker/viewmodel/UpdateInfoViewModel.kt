package com.pbl.kidstracker.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class UpdateInfoViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _phone = MutableLiveData("")
    val phone: LiveData<String> = _phone

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    private val _age= MutableLiveData("")
    val age: LiveData<String> = _age

    private val _gender= MutableLiveData("")
    val gender: LiveData<String> = _gender

    private val _image= MutableLiveData("")
    val image: LiveData<String> = _image

    private val _device= MutableLiveData("")
    val device: LiveData<String> = _device

    fun updateDevice(device: String) {
        _device.value = device
    }

    fun updateImage(image: String) {
        _image.value = image
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updatePhone(newPhone: String) {
        _phone.value = newPhone
    }

    fun updateAge(newAge: String) {
        _age.value = newAge
    }
    fun updateGender(newGender: String) {
        _gender.value = newGender
    }

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    init {
        getUserInfo()
    }

    fun getUserInfo(){
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        val user =
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("Users").child(userid)
        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateName(snapshot!!.child("name").getValue().toString())
                updatePhone(snapshot!!.child("phone number").getValue().toString())
                updateAge(snapshot!!.child("age").getValue().toString())
                updateGender(snapshot!!.child("gender").getValue().toString())
                updateEmail(snapshot!!.child("email").getValue().toString())
                updatePassword(snapshot!!.child("password").getValue().toString())
                updateImage(snapshot!!.child("image").getValue().toString())
                updateDevice(snapshot!!.child("device").getValue().toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("UPDATE", "Failed to read value.", error.toException())
            }
        })
    }
    fun updateUserInfo(userscreen: () -> Unit){
        val name: String? = _name.value?.trim()
        val phone: String? = _phone.value?.trim()
        val age: String? = _age.value?.trim()
        val gender: String? = _gender.value?.trim()
        val email: String? =_email.value?.trim()
        val password: String? = _password.value?.trim()
        val image: String? = _image.value?.trim()
        val device: String? = _device.value?.trim()


        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        val user =
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("Users").child(userid)
        val hashMap = HashMap<String, String>()
        hashMap["email"] = email.toString()
        hashMap["name"] = name.toString()
        hashMap["password"] = password.toString()
        hashMap["phone number"] = phone.toString()
        hashMap["age"] = age.toString()
        hashMap["gender"] = gender.toString()
        hashMap["device"] = device.toString()
        hashMap["image"] = image.toString()
        user.setValue(hashMap)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    userscreen()
                    _status.postValue("Update Successful!")
                }else{
                    Log.d("update", "failed")
                    _status.postValue("Update failed!")
                }
            })
    }
}