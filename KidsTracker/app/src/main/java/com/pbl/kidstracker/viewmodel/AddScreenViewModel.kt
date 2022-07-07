package com.pbl.kidstracker.viewmodel

import android.util.Log
import androidx.compose.animation.core.snap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.model.mutation.Precondition.updateTime
import com.google.firebase.ktx.Firebase
import com.pbl.kidstracker.model.Notification
import com.pbl.kidstracker.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AddScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _contactUserID = MutableLiveData("")
    val contactUserID: LiveData<String> = _contactUserID

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _isHide = MutableLiveData(false)
    val isHide: LiveData<Boolean> = _isHide

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    private val _isChat = MutableLiveData(false)
    val isChat: LiveData<Boolean> = _isChat

    fun updateIsChat(isChat: Boolean) {
        _isChat.value = isChat
    }

    fun updateIsHide(isHide: Boolean?) {
        _isHide.value = isHide
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    private var _listContactUser = MutableLiveData(emptyList<User>().toMutableList())
    val listContactUser: LiveData<MutableList<User>> = _listContactUser

    private fun updateHeartBeatInList(list: MutableList<User>) {
        _listContactUser.value = list.asReversed()
    }
    init {
        getListContactUser()
    }

    fun AddContactUSer(){
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val currentDate = sdf.format(Date())
        Log.d("time" , currentDate)
        val email: String? =_email.value?.trim()

        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid

        var contactUserId: String? = ""
        viewModelScope.launch(Dispatchers.IO) {
            /* get information of contact user*/
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                    .getReference("Users").get()
                    .addOnCompleteListener(OnCompleteListener<DataSnapshot?> { task ->
                        if (task.isSuccessful) {
                            val snapshot = task.result
                            for (child in snapshot.children){
                                val useremail = child.child("email").getValue().toString()
                                val userName = child.child("name").getValue().toString()
                                if (useremail == email) {
                                    contactUserId = child.key.toString()
                                    val hashMap = HashMap<String, String>()
                                    hashMap["name"] = userName
                                    hashMap["email"] = useremail
                                    hashMap["time"] = currentDate
                                    FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                                        .getReference("ContactUser").child(userid).child(contactUserId!!).setValue(hashMap)
                                        .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                                            if (task.isSuccessful) {
                                                updateIsHide(true)
                                            } else{
                                                _status.postValue(task.exception!!.message)
                                            }
                                        })
                                }
                                else {_status.postValue("Email has not yet registered an account. Please try again")}
                            }

                        } else {
                            Log.d("TAG", "LOI")
                        }
                    })
        }
    }

    fun getListContactUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val firebaseUser = auth.currentUser!!
            val userid = firebaseUser!!.uid
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("ContactUser").child(userid)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = emptyList<User>().toMutableList()
                        for (child in snapshot.children){
                            val item = User(" " , " " , " ", " ", " " , " ")
                            item.setuserid(child.key.toString())
                            item.setname(child!!.child("name").getValue().toString())
                            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                                .getReference("Users").child(child.key.toString())
                                .addValueEventListener(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        item.setimage( snapshot.child("image").getValue().toString())
                                        if (item.getimage().toString() == "null"){
                                            item.setimage("https://firebasestorage.googleapis.com/v0/b/esp8266-72053.appspot.com/o/images%2Fdefault_image.jpg?alt=media&token=fce2f4b8-8bf5-474c-8779-8bb06096696e")
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            item.setemail(child!!.child("email").getValue().toString())
                            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/").getReference("Users")
                                .child(item.getuserid().toString()).child("device")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                                            .getReference("device").child(snapshot.getValue().toString()).child("heartbeat").orderByKey().limitToLast(1)
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    var s:String=snapshot!!.getValue().toString()
                                                    var arr:List<String> = s.split("=")
                                                    val s2 = arr[0].substring(1)
                                                    val heartbeat = snapshot!!.child(s2).child("heartbeat").getValue().toString()
                                                    item.setheartbeat(heartbeat)
                                                    val location = snapshot!!.child(s2).child("location").getValue().toString()
                                                    Log.d("a",location)
                                                    if (location != ""){
                                                        item.setlocation(location)
                                                    }
                                                    else{
                                                        item.setlocation("0.0,0.0")
                                                    }
                                                }
                                                override fun onCancelled(error: DatabaseError) {
                                                    Log.d("UPDATE", "Failed to read value.", error.toException())
                                                }
                                            })
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        Log.d("UPDATE", "Failed to read value.", error.toException())
                                    }
                                })
                            list.add(item)
                        }
                        updateHeartBeatInList(list)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
    fun deleteContactUser(id: String){
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        FirebaseDatabase.getInstance().getReference()
            .child("ContactUser").child(userid).child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.removeValue()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("UPDATE", "Failed to read value.", error.toException())
                }
            })
    }
}