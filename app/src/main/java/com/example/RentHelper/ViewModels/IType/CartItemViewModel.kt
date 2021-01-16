package com.example.RentHelper.ViewModels.IType

import android.net.Uri
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class CartItemViewModel(val uri: Uri?,val id:String,val title:String ,val tradeMethod:ArrayList<String>,val amount:Int,val mytype:ArrayList<String> ):IType {
    override fun getType(): String {
        return ViewType.CartItemViewModel
    }
}