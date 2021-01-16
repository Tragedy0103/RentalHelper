package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import org.json.JSONObject

class UserModel (): Observed<UserModel>() {
    var id :String =""
    var email :String =""
    var name :String =""
    var nickName :String =""
    var phone :String =""
    var address :String =""
    var wishListModel:ArrayList<WishItemModel>? =null

    private fun setInfo(jsonObject: JSONObject){
        id =jsonObject.getString("id")
        email =jsonObject.getString("email")
        name =jsonObject.getString("name")
        nickName=jsonObject.getString("nickName")
        phone =jsonObject.getString("phone")
        address  =jsonObject.getString("address")
        wishListModel=WishListModel.createWishList(jsonObject.getJSONArray("wishItems"))
        notify(this)
    }

    fun getInfo(userId: String):UserModel {
        NetworkController.instance.userBasicInfo(userId)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when(code){
                        200->{
                            val jsonObject =JSONObject(res)
                            setInfo(jsonObject)
                        }
                        else ->{}
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