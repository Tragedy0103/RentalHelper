package com.example.RentHelper.ViewModels

import com.example.RentHelper.Tool.*
import com.example.RentHelper.models.AccountModel

class AccountInfoViewModel : Observed<AccountInfoViewModel>(),Observer<AccountModel>{
    companion object{
        val instance:AccountInfoViewModel by lazy {  AccountInfoViewModel()}
    }
    var email :String=""
    var name :String=""
    var phone :String=""
    var address :String=""
    var nickName :String=""
    var codeMessageObserver:CodeMessageObserver=CodeMessageObserver()
    var onModifySuccess:(()->Unit)?= null
    var onModifyFail:(()->Unit)?= null
    init{
        onBind(AccountModel.INSTANCE)
        codeMessageObserver.addCode(200){ _, _ ->
            onModifySuccess?.invoke()
            notify(this)
        }
        codeMessageObserver.addCode(401){ _, _ ->
            onModifyFail?.invoke()
        }
        email = AccountModel.INSTANCE.email
        name = AccountModel.INSTANCE.name
        phone = AccountModel.INSTANCE.phone
        nickName = AccountModel.INSTANCE.nickName
        address = AccountModel.INSTANCE.address
    }
    fun InfoModify(name:String,phone:String,nickName:String,address:String){
        codeMessageObserver.onBind(AccountModel.INSTANCE.userInfoModify(name,nickName,phone,address))
    }

    override fun onRegister() {
        notify(this)
    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
        AccountModel.INSTANCE.unregister(this)
    }

    override fun onBind(observed: Observed<AccountModel>) {
        observed.register(this)
    }

    override fun update(data: AccountModel) {
        email = data.email
        name = data.name
        phone = data.phone
        nickName = data.nickName
        address = data.address
        notify(this)
    }
}