package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.Activity.AddProductActivity
import com.example.RentHelper.ViewModels.IType.MyOrderPageViewModel
import com.example.RentHelper.ViewModels.IType.MyStorePageViewModel
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*

class MyStoreFragmentViewModel : Observed<MyStoreFragmentViewModel>() {
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<MyStorePageViewModel>(
                LayoutInflater.from(it.context)
                    .inflate(R.layout.page_shop_header_recyclerview, it, false)
            ) {
                override fun bind(item: MyStorePageViewModel) {
                    val imgAdd = itemView.findViewById<ImageView>(R.id.imgProductAdd)
                    imgAdd.setOnClickListener {
                        Router.router(
                            Router.NextTmp(
                                itemView.context!!,
                                AddProductActivity::class.java, null
                            )
                        )
                    }
                    if (item.str != "OnShelf") {
                        imgAdd.visibility = View.GONE
                    } else {
                        imgAdd.visibility = View.VISIBLE
                    }

                    val rcvGameShop = itemView.findViewById<RecyclerView>(R.id.rcvGameShop)
                    rcvGameShop.adapter = item.adapter
                    rcvGameShop.layoutManager = LinearLayoutManager(itemView.context)
                }
            }
        }, ViewType.MyStorePageViewModel)
        .addType({
            object : BaseViewHolder<MyOrderPageViewModel>(
                LayoutInflater.from(it.context)
                    .inflate(R.layout.page_shop_recyclerview, it, false)
            ) {
                override fun bind(item: MyOrderPageViewModel) {
                    val rcvGameShop = itemView.findViewById<RecyclerView>(R.id.rcvGameShop)
                    rcvGameShop.adapter = item.adapter
                    rcvGameShop.layoutManager = LinearLayoutManager(itemView.context)
                    item.getSaleProductList()
                }
            }
        }, ViewType.MyOrderPageViewModel)
        .build()
    fun updata(){
        adapter.map {
            if(it is MyStorePageViewModel){
                it.getProductList()
            }
            if(it is MyOrderPageViewModel){
                it.getSaleProductList()
            }
        }
    }
    fun addItem(item: IType): MyStoreFragmentViewModel {
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