package com.pbl.kidstracker.model

class Heartbeat {

    private var time: String? = ""
    private var heartbeat: String? = ""
    private var location: String? = ""

    constructor(time: String?, heartbeat: String?) {
        this.time = time
        this.heartbeat = heartbeat
    }

    constructor(time: String?, heartbeat: String?, location: String?) {
        this.time = time
        this.heartbeat = heartbeat
        this.location = location
    }

    fun setTime(Time: String) {
        this.time = Time
    }
    fun getTime(): String? {
        return time
    }

    fun setHeartbeat(Heartbeat: String) {
        this.heartbeat = Heartbeat
    }
    fun getHeartbeat(): String? {
        return heartbeat
    }

    fun setLocation(location: String) {
        this.location = location
    }
    fun getLocation(): String? {
        return location
    }

}