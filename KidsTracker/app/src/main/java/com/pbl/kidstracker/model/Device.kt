package com.pbl.kidstracker.model

import com.google.firebase.database.Exclude

public class Device {

    var heartbeat: String? = null
    var location: String? = null
    var userId: String? = null
    var starCount = 0
    var stars: Map<String, String> = HashMap()

    fun Device() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    fun Device(heartbeat: String?, location: String?, userId: String?) {
        this.heartbeat = heartbeat
        this.location = location
        this.userId = userId
    }

    @Exclude
    fun toMap(): Map<String, Any?>? {
        val result: HashMap<String, Any?> = HashMap()
        result["heartbeat"] = heartbeat
        result["location"] = location
        result["userId"] = userId
        return result
    }
}