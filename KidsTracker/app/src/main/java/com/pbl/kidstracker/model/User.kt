package com.pbl.kidstracker.model

public class User {

    private var userid: String? = ""
    private var name: String? = ""
    private var image: String? = ""
    private var email: String? = ""
    private var heartBeat: String? = ""
    private var location: String? = ""

    constructor(userid: String?, name: String?, image: String?, email: String?) {
        this.userid = userid
        this.name = name
        this.image = image
        this.email = email
    }

    constructor(
        userid: String?,
        name: String?,
        image: String?,
        email: String?,
        heartBeat: String?,
        location: String?
    ) {
        this.userid = userid
        this.name = name
        this.image = image
        this.email = email
        this.heartBeat = heartBeat
        this.location = location
    }

    fun setuserid(userid: String) {
        this.userid = userid
    }
    fun getuserid(): String? {
        return userid
    }
    fun setname(name: String) {
        this.name = name
    }
    fun getname(): String? {
        return name
    }
    fun setimage(image: String) {
        this.image = image
    }
    fun getimage(): String? {
        return image
    }
    fun setemail(email: String) {
        this.email = email
    }
    fun getemail(): String? {
        return email
    }
    fun setheartbeat(heartbeat: String) {
        this.heartBeat = heartbeat
    }
    fun getheartbeat(): String? {
        return heartBeat
    }
    fun setlocation(location: String) {
        this.location = location
    }
    fun getlocation(): String? {
        return location
    }


}