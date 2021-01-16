package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.ViewModels.IType.MyOrderPageViewModel
import com.example.RentHelper.R
import com.example.RentHelper.Tool.BaseViewHolder
import com.example.RentHelper.Tool.CommonAdapter
import com.example.RentHelper.Tool.ViewType

class AccountOrderViewModel {
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<MyOrderPageViewModel>(
                LayoutInflater.from(it.context)
                    .inflate(R.layout.page_shop_recyclerview, it, false)
            ) {
                override fun bind(item: MyOrderPageViewModel) {
                    val rcvGameShop = itemView.findViewById<RecyclerView>(R.id.rcvGameShop)
                    rcvGameShop.adapter = item.adapter
                    rcvGameShop.layoutManager = LinearLayoutManager(itemView.context)
                    item.getBuyProductList()
                }
            }
        }, ViewType.MyOrderPageViewModel)
        .build()
    fun addItem(item: MyOrderPageViewModel) {
        adapter.add(item)
    }
}