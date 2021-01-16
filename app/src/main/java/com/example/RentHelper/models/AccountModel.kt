package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.CodeMessage
import com.example.RentHelper.Tool.CodeMessage.*
import com.example.RentHelper.Tool.Observed
import org.json.JSONObject

class AccountModel : Observed<AccountModel>() {
    enum class Stage {
        LOGIN,
        UNLOGIN
    }

    companion object {
        val INSTANCE: AccountModel by lazy {
            AccountModel()
        }
    }

    var stage: Stage = Stage.UNLOGIN
    var token: String? = null
    var id: String = ""
    var email: String = ""
    var name: String = ""
    var nickName: String = ""
    var phone: String = ""
    var address: String = ""
    var products: ArrayList<ProductModel> = ArrayList()
    val wishItems: ArrayList<WishItemModel> = ArrayList()
    var deviceToken:String = ""


    fun getUserInfo(token: String) {
        NetworkController.instance.userInfo(token)
            .onResponse { code, res ->
                Log.d("userInfo", res)
                val jsonObject = JSONObject(res)
                this.token = token
                id = jsonObject.getString("id")
                name = jsonObject.getString("name")
                email = jsonObject.getString("email")
                phone = jsonObject.getString("phone")
                nickName = jsonObject.getString("nickName")
                address = jsonObject.getString("address")
                products.addAll(ProductsListModel.getProductList(jsonObject.getJSONArray("products")))
                wishItems.addAll(WishListModel.createWishList(jsonObject.getJSONArray("wishItems")))
                Handler(Looper.getMainLooper()).post {
                    notify(this)
                }
            }
            .onFailure { }
            .onComplete { }
            .exec()
    }

    fun userInfoModify(
        name: String,
        nickname: String,
        phone: String,
        address: String
    ): CodeMessage {
        val codeMessage = CodeMessage()
        token?.let {
            NetworkController.instance.userInfoModify(it, name, nickname, phone, address)
                .onResponse { code, res ->
                    getUserInfo(it)
                    Handler(Looper.getMainLooper()).post {
                        codeMessage.setCodeMessage(Type.USERINFO_MODIFY, code, res)
                    }
                }
                .exec()
        }
        return codeMessage
    }
    fun logout(){
        stage = Stage.UNLOGIN
        token = null
        id =""
        email = ""
        name = ""
        nickName = ""
        phone = ""
        address = ""
        products.clear()
        wishItems.clear()
        deviceToken = ""
    }
    fun login(email: String, password: String,deviceToken:String): CodeMessage {
        val codeMessage = CodeMessage()
        NetworkController.instance.login(email, password,deviceToken)
            .onResponse { code, res ->
                when (code) {
                    200 -> {
                        this.stage = Stage.LOGIN
                        getUserInfo(res)
                    }
                }
                Handler(Looper.getMainLooper()).post {
                    codeMessage.setCodeMessage(Type.LOGIN, code, res)
                    notify(this)
                }
            }
            .exec()
        return codeMessage
    }

    fun register(
        email: String,
        verityCode: String,
        password: String,
        name: String,
        phone: String
    ): CodeMessage {
        val codeMessage = CodeMessage()
        NetworkController.instance.register(email, verityCode, password, name, phone)
            .onResponse { code, res ->
                when (code) {
                    200 -> {
                        this.token = res
                    }
                }
                Handler(Looper.getMainLooper()).post {
                    codeMessage.setCodeMessage(Type.REGISTER, code, res)
                    notify(this@AccountModel)
                }
            }
            .exec()
        return codeMessage
    }

    fun modify(email: String, verityCode: String, password: String): CodeMessage {
        val codeMessage = CodeMessage()
        NetworkController.instance.modify(email, verityCode, password)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    codeMessage.setCodeMessage(Type.MODIFY, code, res)
                    notify(this)
                }
            }
            .exec()
        return codeMessage
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {

    }
}