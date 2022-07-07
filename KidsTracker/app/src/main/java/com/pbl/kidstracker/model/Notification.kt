package com.pbl.kidstracker.model

class Notification {
    private var notify: String? = ""
    private var heartbeat: String? = ""
    private var time : String? = ""

    constructor(notify: String?, heartbeat: String?, time: String?) {
        this.notify = notify
        this.heartbeat = heartbeat
        this.time = time
    }
    fun setnotify(notify: String) {
        this.notify = notify
    }
    fun getnotify(): String? {
        return notify
    }
    fun setheartbeat(heartbeat: String) {
        this.heartbeat = heartbeat
    }
    fun getheartbeat(): String? {
        return heartbeat
    }
    fun settime(time: String) {
        this.time =time
    }
    fun gettime(): String? {
        return time
    }
}