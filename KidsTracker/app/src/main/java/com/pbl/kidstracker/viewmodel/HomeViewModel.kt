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
import com.google.firebase.ktx.Firebase
import com.pbl.kidstracker.model.Heartbeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel: ViewModel(){
    private val auth: FirebaseAuth = Firebase.auth

    private val _time = MutableLiveData("")
    val time: LiveData<String> = _time

    private val _heartbeat = MutableLiveData("")
    val heartbeat: LiveData<String> = _heartbeat

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double> = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double> = _lon


    fun updateTime(time: String) {
        _time.value = time
    }

    fun updateHeartbeat(heartbeat: String) {
        _heartbeat.value = heartbeat
    }


    fun updateLat(lat: Double) {
        _lat.value = lat
    }

    fun updateLon(lon: Double) {
        _lon.value = lon
    }

    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    private var _listHeartBeat = MutableLiveData(emptyList<Heartbeat>().toMutableList())
    val listHeartBeat: LiveData<MutableList<Heartbeat>> = _listHeartBeat

    private fun updateHeartBeatInList(list: MutableList<Heartbeat>) {
        _listHeartBeat.value = list.asReversed()
    }

    init {
        getHeartbeat()
        getList()
    }

    fun getHeartbeat(){
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        val device = FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
            .getReference("device")
        FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/").getReference("Users")
            .child(userid).child("device")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    device.child(snapshot.getValue().toString()).child("heartbeat").orderByKey().limitToLast(1)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var s:String=snapshot!!.getValue().toString()
                            var arr:List<String> = s.split("=")
                            val s2 = arr[0].substring(1)
                            updateTime(s2)
                            updateHeartbeat(snapshot!!.child(s2).child("heartbeat").getValue().toString())
                            try {
                                val location = snapshot!!.child(s2).child("location").getValue().toString()
                                val latlon: List<String> = location.split(",")
                                val lat = latlon[0].toDouble()
                                val lon = latlon[1].toDouble()
                                updateLat(lat)
                                updateLon(lon)
                            } catch (e: Exception) {
                                updateLat(0.0)
                                updateLon(0.0)
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
    }
    fun sendCurrentHeartBeat(){
        val time: String? = _time.value?.trim()
        val heartbeat: String? = _heartbeat.value?.trim()

        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        val user =
            FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                .getReference("listheartbeat").child(userid)
        val hashMap = HashMap<String, String>()
        hashMap["heartbeat"] = heartbeat.toString()
        user.child(time.toString()).setValue(hashMap)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    _status.postValue("Successful!")
                } else{
                    _status.postValue("Failed!")
                }
            })
    }
    fun getList(){
        viewModelScope.launch(Dispatchers.IO) {
            val firebaseUser = auth.currentUser!!
            val userid = firebaseUser!!.uid
            val user =
                FirebaseDatabase.getInstance("https://esp8266-72053-default-rtdb.firebaseio.com/")
                    .getReference("listheartbeat").child(userid)
            user.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = emptyList<Heartbeat>().toMutableList()
                    for (child in snapshot.children) {
                        val item = Heartbeat(" ", " ")
                        var time = child!!.key.toString().replace('T' , ' ').replace('Z' , ' ').replace('-', '/')
                        item.setTime(time)
                        item.setHeartbeat(child!!.child("heartbeat").getValue().toString())
                        list.add(item)
                    }
                    updateHeartBeatInList(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("UPDATE", "Failed to read value.", error.toException())
                }
            })
        }
    }
    fun deleteIteminListHeartbeat(time: String){
        var deteletime = time.substring(0,time.length - 1).replace(' ' , 'T').replace('/', '-')
        deteletime = deteletime + "Z"
        val firebaseUser = auth.currentUser!!
        val userid = firebaseUser!!.uid
        FirebaseDatabase.getInstance().getReference()
            .child("listheartbeat").child(userid).child(deteletime)
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





