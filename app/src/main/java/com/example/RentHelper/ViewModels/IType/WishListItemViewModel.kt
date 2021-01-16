package com.example.RentHelper.ViewModels.IType

import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class WishListItemViewModel(val id:String?,val name :String ,val value:Double ,val amount:Int) : IType {
    override fun getType(): String {
        return ViewType.WishListItemViewModel
    }
}