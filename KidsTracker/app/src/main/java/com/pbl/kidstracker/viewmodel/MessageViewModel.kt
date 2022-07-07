package com.pbl.kidstracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pbl.kidstracker.model.Message
import com.pbl.kidstracker.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _contactUserID = MutableLiveData("")
    val contactUserID: LiveData<String> = _contactUserID

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    fun updateMessage(message: String) {
        _message.value = message
    }

    private var _messages = MutableLiveData(emptyList<Message>().toMutableList())
    val messages: LiveData<MutableList<Message>> = _messages

    private fun updateMessages(list: MutableList<Message>) {
        _messages.value = list.asReversed()
    }

    init {
        contactUserID.value?.let { getMessagesByID(it) }
    }
    fun SendMessage(contacUserID: String){
        val message : String? = _message.value?.trim()

        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid

        viewModelScope.launch(Dispatchers.IO) {
            val hashMap = HashMap<String, String>()
            hashMap["message"] = message.toString()
            hashMap["receiver"] = contacUserID
            hashMap["sender"] = userid
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("Chats").push().setValue(hashMap)
                .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    if (task.isSuccessful) {
                        _message.value = " "
                    } else{
                        Log.d("error", "failed")
                    }
                })
        }
    }
     fun getMessagesByID(contactUserID: String) {
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("Chats")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = emptyList<Message>().toMutableList()
                        for (child in snapshot.children){
                            val mess = Message(" " , " " , " ")
                            if (
                                (child!!.child("sender").getValue().toString() == userid && child!!.child("receiver").getValue().toString() == contactUserID)
                                || (child!!.child("sender").getValue().toString() == contactUserID && child!!.child("receiver").getValue().toString() == userid )
                            ){
                                mess.setsender(child!!.child("sender").getValue().toString())
                                mess.setreceiver(child!!.child("receiver").getValue().toString())
                                mess.setmessage(child!!.child("message").getValue().toString())
                                if (child!!.child("sender").getValue().toString() == userid)
                                {
                                    mess.setIsCurrentUser(true)
                                }
                                else{
                                    mess.setIsCurrentUser(false)
                                }
                                list.add(mess)
                            }

                        }
                        updateMessages(list)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}