package com.example.RentHelper.ViewModels.IType

import android.view.LayoutInflater
import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.Tool.CommonAdapter
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.R
import com.example.RentHelper.ViewHolder.MainNewsViewHolder

class MainFragmentViewModel :Observed<MainFragmentViewModel>(),IType {
    val adapter:CommonAdapter= CommonAdapter.Builder(ArrayList())
        .addType({viewGroup ->
            MainNewsViewHolder(
                LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_list_news,viewGroup,false))
        }, ViewType.MainNewsCardViewModel)
        .build()
    fun addItem(item : MainNewsCardViewModel): MainFragmentViewModel {
        adapter.add(item.getProductListByType(0,50))
        notify(this)
        return this
    }
    override fun getType(): String {
        return ViewType.MainFragmentViewModel
    }

    override fun onRegister() {
    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
        adapter.clear()
    }
}