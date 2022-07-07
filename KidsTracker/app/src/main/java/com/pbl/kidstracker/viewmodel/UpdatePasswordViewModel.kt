package com.pbl.kidstracker.viewmodel


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

class UpdatePasswordViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _oldPassword = MutableLiveData("")
    val oldPassword: LiveData<String> = _oldPassword

    fun updateOldPassword(oldPassword: String) {
        _oldPassword.value = oldPassword
    }

    private val _newPassword = MutableLiveData("")
    val newPassword: LiveData<String> = _newPassword

    fun updateNewPassword(newPassword: String) {
        _newPassword.value = newPassword
    }

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

     fun changePassword(loginscreen: () -> Unit){
         val newPassword: String? = _newPassword.value?.trim()
         val oldPassword: String? = _oldPassword.value?.trim()
//         var checkIsEqualPassword: Boolean? = false

         val firebaseUser = auth.currentUser!!
         val userid = firebaseUser!!.uid
         val user =
             FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                 .getReference("Users").child(userid)
         user.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val currentPassword = snapshot!!.child("password").getValue().toString()
                 if (oldPassword!!.equals(currentPassword)){
//                     checkIsEqualPassword = true
                     if (newPassword != null) {
                         firebaseUser!!.updatePassword(newPassword.trim())
                             .addOnCompleteListener { task ->
                                 if (task.isSuccessful){
                                     user.child("password").setValue(newPassword)
                                     loginscreen()
                                 }
                                 else {
                                     _status.postValue("Change password failure")
                                 }
                             }
                     }
                 }
                 else{_status.postValue("Wrong Password")}
             }
             override fun onCancelled(error: DatabaseError) {

             }
         })

     }
}