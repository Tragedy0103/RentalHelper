package com.example.RentHelper.ViewModels.IType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.RentHelper.Activity.ProductActivity
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.models.ProductsListModel
import com.facebook.drawee.view.SimpleDraweeView

class GameShopPageViewModel(val type1: String, val type2: String, val type3: String) : IType {
    val firstLoad = object : ObserverSomethings<ProductsListModel>() {
        override fun update(data: ProductsListModel) {
            data.items.forEach {
                adapter.add(
                    MyStoreItemViewModel(
                        it.getFirstImg(),
                        it.id,
                        it.title,
                        it.amount,
                        it.getMySaleType(),
                        it.getPrice()
                    )
                )
            }
        }
    }
    val loadAmount = 4
    var offSet = 0
    val beforeObserver = object : ObserverSomethings<ProductsListModel>() {
        override fun update(data: ProductsListModel) {
            for (i in data.items.size - 1 downTo 0) {
                val product = data.items[i]
                adapter.add(
                    0, MyStoreItemViewModel(
                        product.getFirstImg(),
                        product.id,
                        product.title,
                        product.amount,
                        product.getMySaleType(),
                        product.getPrice()
                    )
                )
                adapter.remove(adapter.getItemSize() - 1)
                offSet--
            }
        }
    }
    val afterObserver = object : ObserverSomethings<ProductsListModel>() {
        override fun update(data: ProductsListModel) {
            data.items.forEach {
                adapter.add(
                    MyStoreItemViewModel(
                        it.getFirstImg(),
                        it.id,
                        it.title,
                        it.amount,
                        it.getMySaleType(),
                        it.getPrice()
                    )
                )
            }
            //每當載入超過一定數量後就把反方向的資料做刪除
            //刪除後調整偏移量
            if (adapter.itemCount > loadAmount*4) {
                for (i in 0 until loadAmount) {
                    adapter.remove(0)
                    offSet++
                }
            }
        }
    }
    fun loadBefore(position: Int) {
        if (position - loadAmount<= 0) {
            beforeObserver.onBind(
                ProductsListModel().getProductList(
                    type1,
                    type2,
                    type3,
                    1,
                    position-1
                )
            )
        } else {
            beforeObserver.onBind(
                ProductsListModel().getProductList(
                    type1,
                    type2,
                    type3,
                    position- loadAmount,
                    loadAmount
                )
            )
        }
    }
    fun loadAfter(position: Int) {
        afterObserver.onBind(
            ProductsListModel().getProductList(
                type1,
                type2,
                type3,
                position + 1,
                loadAmount
            )
        )
    }
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<MyStoreItemViewModel>(
                LayoutInflater.from(it.context).inflate(R.layout.item_list_product, it, false)
            ) {
                override fun bind(item: MyStoreItemViewModel) {
                    itemView.findViewById<ImageView>(R.id.imgSet).visibility = View.GONE
                    itemView.findViewById<SimpleDraweeView>(R.id.imgProduct)
                        .setImageURI(item.uri, itemView.context)
                    itemView.findViewById<TextView>(R.id.tvTitle).text = item.title
                    itemView.findViewById<TextView>(R.id.tvPrice).text = item.price

                    val llType = itemView.findViewById<LinearLayout>(R.id.llType)

                    llType.removeAllViews()
                    item.saleType.forEach {str->
                        val view = LayoutInflater.from(itemView.context).inflate(R.layout.item_tab_text,llType,false)
                        view.findViewById<TextView>(R.id.tvTab).text = str
                        llType.addView(view)
                    }
                    itemView.findViewById<TextView>(R.id.tvAmount).text = item.amount.toString()
                    itemView.setOnClickListener {
                        Router.router(Router.NextTmp(itemView.context, ProductActivity::class.java,
                            Bundle().apply { putString("ProductId", item.id) }
                        ))
                    }
                }
            }
        }, ViewType.MyStoreItemViewModel)
        .build()

    init {
        updata()
        adapter.onBind = { position ->
            //當資料綁定位置到達載入後續資料的錨點後開始載入資料
            //動態載入分成載入前半部跟後半部
            //根據我們後端的服務我們需要紀錄載入資料庫的第x筆後續的n筆資料
            //所以我們需要設置偏移量來和目前資料位置做校正
            if (position + offSet != loadAmount) {
                //當綁定位置到達錨點後載入資料
                if (position == adapter.itemCount - loadAmount) {
                    loadAfter(offSet + adapter.getItemSize())
                } else if (position == loadAmount) {
                    loadBefore(offSet+1)
                }
            }
        }
    }

    fun updata() {
        adapter.clear()
        firstLoad.close()
        firstLoad.onBind(ProductsListModel().getProductList(type1, type2, type3, 0, 10))
    }

    override fun getType(): String {
        return ViewType.GameShopPageViewModel
    }
}