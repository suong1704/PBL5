package com.pbl.kidstracker.model

class Message {
    private var message: String? = ""
    private var receiver : String? = ""
    private var sender: String? = ""
    private var isCurrentUser: Boolean = false

    constructor(message: String?, receiver: String?, sender: String?) {
        this.message = message
        this.receiver = receiver
        this.sender = sender
    }

    fun setmessage(message: String) {
        this.message = message
    }
    fun getmessage(): String? {
        return message
    }
    fun setreceiver(receiver: String) {
        this.receiver =receiver
    }
    fun getreceiver(): String? {
        return receiver
    }
    fun setsender(sender: String) {
        this.sender = sender
    }
    fun getsender(): String? {
        return sender
    }
    fun setIsCurrentUser(isCurrentUser: Boolean) {
        this.isCurrentUser = isCurrentUser
    }
    fun getIsCurrentUser(): Boolean? {
        return isCurrentUser
    }
}