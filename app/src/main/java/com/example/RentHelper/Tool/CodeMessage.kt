package com.example.RentHelper.Tool

import android.util.Log
import com.example.RentHelper.Tool.CodeMessage.*

class CodeMessage : Observed<CodeMessage>() {
    enum class Type {
        LOGIN,
        REGISTER,
        MODIFY,
        USERINFO_MODIFY;
    }

    var type: Type? = null
    var code: Int? = null
    var message: String? = null
    fun setCodeMessage(type: Type, code: Int, message: String) {
        this.type = type
        this.code = code
        this.message = message
        notify(this)
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {

    }
}

class CodeMessageObserver : Observer<CodeMessage> {
    var observeds:ArrayList<Observed<CodeMessage>> = ArrayList()
    val codemap: HashMap<Int, (message:String,type: Type) -> Unit> = HashMap()
    fun distroy(){
        observeds.forEach {
            it.unregister(this)
        }
        observeds.clear()
    }
    fun addCode(code: Int, action: (message:String,type: Type) -> Unit):CodeMessageObserver {
        codemap[code] = action
        return this
    }

    override fun onBind(observed: Observed<CodeMessage>) {
        observed.register(this)
        observeds.add(observed)
    }

    override fun update(data: CodeMessage) {
        Log.d("CodeMessage",""+data.code+data.message)
        codemap[data.code]?.invoke(data.message!!,data.type!!)
    }

}