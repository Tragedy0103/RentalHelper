package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.Tool.CommonAdapter
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.R
import com.example.RentHelper.ViewHolder.MenuViewHolder
import com.example.RentHelper.ViewModels.IType.MenuItemViewModel

class MenuFragmentViewModel  {
    val items = ArrayList<IType>()
    val adapter =CommonAdapter.Builder(items)
        .addType({viewGroup ->
            MenuViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_menu, viewGroup, false))
        }, ViewType.MenuItemViewModel)
        .build()
    fun addItem(item : MenuItemViewModel):MenuFragmentViewModel{
        this.items.add(item)
        return this
    }
}