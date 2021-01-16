package com.example.RentHelper.ViewModels.IType

import android.net.Uri
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class MyStoreItemViewModel(val uri: Uri?,val id:String,val title :String , val amount:Int ,val saleType:ArrayList<String> ,val price :String):IType {

    override fun getType(): String {
        return ViewType.MyStoreItemViewModel
    }
}