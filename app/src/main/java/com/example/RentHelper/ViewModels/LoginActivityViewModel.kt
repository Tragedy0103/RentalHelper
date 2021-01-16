package com.example.RentHelper.ViewModels

import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.CodeMessage.Type.*
import com.example.RentHelper.Tool.CodeMessageObserver
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.models.AccountModel

class LoginActivityViewModel : Observed<LoginActivityViewModel>() {

    var CodeMessageObserver:CodeMessageObserver? = CodeMessageObserver()


    var onLoginSuccess: (() -> Unit)? = null
    var onRegisterSuccess: (() -> Unit)? = null
    var onModifySuccess: (() -> Unit)? = null

    var errorText: String? = null
        set(value) {
            field = value
            notify(this)
        }

    init {
        CodeMessageObserver?.addCode(200) { message, type ->
            when  (type){
                REGISTER ->{onRegisterSuccess?.invoke()}
                LOGIN ->{onLoginSuccess?.invoke()}
                MODIFY->{onModifySuccess?.invoke()}
                else->{}
            }
        }
            ?.addCode(400){message, type ->
                when  (type){
                    REGISTER ->{errorText = message}
                    MODIFY->{errorText = "認證失敗，請確認信箱或認證碼正確"}
                    else -> {}
                }
            }
            ?.addCode(404 ){message, type ->
                when  (type){
                    LOGIN ->{errorText = "資料或密碼輸入錯誤"}
                    else -> {}
                }
            }
    }

    fun login(email: String, password: String,deviceToken:String) {
        this.CodeMessageObserver?.onBind(AccountModel.INSTANCE.login(email, password,deviceToken))
    }

    fun register(
        email: String,
        verityCode: String,
        password: String,
        name: String,
        phone: String
    ) {
        this.CodeMessageObserver?.onBind(AccountModel.INSTANCE.register(email, verityCode, password, name, phone))
    }

    fun modify(email: String, verityCode: String, password: String) {
        this.CodeMessageObserver?.onBind(AccountModel.INSTANCE.modify(email, verityCode, password))
    }

    fun sendVeritycode(email: String) {
        NetworkController.instance.sendVerityCode(email).exec()
    }

    override fun onRegister() {
    }

    override fun onUnRegister() {
    }

    override fun onCloseObserved() {
        onModifySuccess = null
        onRegisterSuccess = null
        onLoginSuccess = null
        CodeMessageObserver?.distroy()
        CodeMessageObserver = null
    }
}