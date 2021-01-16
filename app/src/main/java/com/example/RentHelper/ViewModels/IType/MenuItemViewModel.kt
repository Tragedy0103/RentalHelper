package com.example.RentHelper.ViewModels.IType

import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.Observed

class MenuItemViewModel(var title:String) : IType, Observed<MenuItemViewModel>() {
    val items:ArrayList<IType> = ArrayList()

    fun addItems(item: String ,action:(()->Unit)): MenuItemViewModel {
        this.items.add(MenuItemTextViewModel(item,action))
        notify(this)
        return this
    }
    override fun getType(): String {
        return ViewType.MenuItemViewModel
    }

    override fun onRegister() {
    }

    override fun onUnRegister() {
    }

    override fun onCloseObserved() {
        items.clear()
    }

}