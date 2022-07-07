package com.pbl.kidstracker.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.pbl.kidstracker.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotifyViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _contactUserID = MutableLiveData("")
    val contactUserID: LiveData<String> = _contactUserID

    fun updateContactUserID(contactUserID: String) {
        _contactUserID.value = contactUserID
    }

    private var _notifications = MutableLiveData(emptyList<Notification>().toMutableList())
    val notifications: LiveData<MutableList<Notification>> = _notifications


    private fun updateNotifications(list: MutableList<Notification>) {
        _notifications.value = list.asReversed()
    }
    init {
        contactUserID?.value?.let { getNotificationByID(it) }
    }
     fun getNotificationByID(contactUserID: String){
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/").getReference("ContactUser")
                .child(userid).child(contactUserID).child("time")
                .addValueEventListener(object:  ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val time = snapshot!!.getValue().toString()
                        FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/").getReference("Users")
                            .child(contactUserID).child("device")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/").getReference("device")
                                        .child(snapshot!!.getValue().toString()).child("message")
                                        .addValueEventListener(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                val list = emptyList<Notification>().toMutableList()
                                                for (child in snapshot.children){
                                                    val notification = Notification(" " , " " , " ")
                                                    if (child.key.toString().compareTo(time, ignoreCase = true) >= 0){
                                                        val time = child.key.toString().replace('T' , ' ').replace('Z' , ' ').replace('-', '/')
                                                        val noti = child.child("message").getValue().toString()
                                                        var arr:List<String> = noti.split(": ")
                                                        notification.settime(time)
                                                        notification.setnotify(arr[0])
                                                        notification.setheartbeat(arr[1])
                                                        list.add(notification)
                                                    }
                                                }
                                                updateNotifications(list)
                                            }
                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }
                                        })
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}