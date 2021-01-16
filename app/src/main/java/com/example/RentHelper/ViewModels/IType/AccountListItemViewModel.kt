package com.example.RentHelper.ViewModels.IType

import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class AccountListItemViewModel(val str:String):IType {
    var action :(()->Unit)?=null

    fun setAction(action:()->Unit): AccountListItemViewModel {
        this.action=action
        return this
    }

    override fun getType(): String {
        return ViewType.AccountListItemViewModel
    }
}