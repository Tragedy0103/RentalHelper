package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class NoteModel() : Observed<NoteModel>() {
    var orderId: String? = null
    var senderId: String? = null
    var senderName: String? = null
    var message: String? = null
    var date: String? = null
    var time: String? = null

    constructor(
        orderId: String,
        senderId: String,
        senderName: String,
        message: String,
        date: String,
        time: String
    ) : this() {
        this.orderId = orderId
        this.senderId = senderId
        this.senderName = senderName
        this.message = message
        this.date = date
        this.time = time
    }

    companion object {
        fun getNoteList(
            token: String,
            orderId: String,
            action: (notelist: ArrayList<NoteModel>) -> Unit
        ) {
            NetworkController.instance.getNoteList(token, orderId)
                .onResponse { code, res ->
                    when (code) {
                        200 -> {
                            Log.d("print", res)
                            val jsonArray = JSONArray(res)
                            Handler(Looper.getMainLooper()).post {
                                action.invoke(toNoteList(jsonArray))
                            }
                        }
                        else->{
                            Log.d("print", code.toString())
                        }
                    }

                }
                .exec()
        }

        fun toNoteList(jsonArray: JSONArray): ArrayList<NoteModel> {
            val tmp = ArrayList<NoteModel>()
            for (i in 0 until jsonArray.length()) {
                tmp.add(toNote(jsonArray.getJSONObject(i)))
            }
            return tmp
        }

        fun toNote(jsonObject: JSONObject): NoteModel {
            val tmp = jsonObject.getString("createTime").split("T")
            return NoteModel(
                jsonObject.getString("orderId"),
                jsonObject.getString("senderId"),
                jsonObject.getString("senderName"),
                jsonObject.getString("message"),
                tmp[0],
                tmp[1]
            )
        }
    }

    fun strToDate(str: String) {
        date = str.split("T")[0]
        time = str.split("T")[1]
    }

    fun addNote(
        token: String,
        orderId: String,
        message: String,
        onSuccess: (str: String) -> Unit?,
        onFail: (str: String) -> Unit?
    ): NoteModel {
        NetworkController.instance.addNote(token, orderId, message)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonObject = JSONObject(res)
                            this.orderId = jsonObject.getString("orderId")
                            this.senderId = jsonObject.getString("senderId")
                            this.senderName = jsonObject.getString("senderName")
                            this.message = jsonObject.getString("message")
                            strToDate(jsonObject.getString("createTime"))
                            onSuccess(res)
                            notify(this)
                        }
                        else -> {
                            onFail.invoke("留言失敗")
                        }
                    }
                }
            }
            .exec()
        return this
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {
    }

    override fun onCloseObserved() {
    }

}