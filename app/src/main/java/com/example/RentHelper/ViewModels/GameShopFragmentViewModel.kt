package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.BaseViewHolder
import com.example.RentHelper.Tool.CommonAdapter
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.ViewModels.IType.GameShopPageViewModel

class GameShopFragmentViewModel:Observed<GameShopFragmentViewModel>() {
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<GameShopPageViewModel>(
                LayoutInflater.from(it.context)
                    .inflate(R.layout.page_shop_recyclerview, it, false)
            ) {
                override fun bind(item: GameShopPageViewModel) {
                    val rcvGameShop = itemView.findViewById<RecyclerView>(R.id.rcvGameShop)
                    rcvGameShop.adapter = item.adapter
                    rcvGameShop.layoutManager = LinearLayoutManager(itemView.context)
                }
            }
        }, ViewType.GameShopPageViewModel)
        .build()
    fun updata(){
        adapter.map {
            if (it is GameShopPageViewModel){
                it.updata()
            }
        }
    }
    fun addItem(item: GameShopPageViewModel):GameShopFragmentViewModel{
        adapter.add(item)
        return this
    }
    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {

    }
}