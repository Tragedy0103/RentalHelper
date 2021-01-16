package com.example.RentHelper.ViewModels.IType

import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.Tool.IType

class MenuItemTextViewModel(val text: String,val action:()->Unit):IType {
    override fun getType()= ViewType.MenuItemTextViewModel
}