package com.pbl.kidstracker.model

class Location {
    private var deviceID: String? = ""
    private var lat: String? = ""
    private var lon: String? = ""

    constructor(deviceID: String?, lat: String?, lon: String?) {
        this.deviceID = deviceID
        this.lat = lat
        this.lon = lon
    }

    fun setID(deviceID: String) {
        this.deviceID = deviceID
    }
    fun getID(): String? {
        return deviceID
    }

    fun setlat(deviceID: String) {
        this.deviceID = deviceID
    }
    fun getlat(): String? {
        return lat
    }
    fun setlon(lon: String) {
        this.lon = lon
    }
    fun getlon(): String? {
        return lon
    }
}