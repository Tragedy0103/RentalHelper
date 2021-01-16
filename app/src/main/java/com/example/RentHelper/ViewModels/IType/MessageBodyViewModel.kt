package com.example.RentHelper.ViewModels.IType

import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class MessageBodyViewModel(var name:String,var date:String?,var time:String?,var message:String): IType{
    override fun getType(): String {
        return ViewType.MessageBodyViewModel
    }

}