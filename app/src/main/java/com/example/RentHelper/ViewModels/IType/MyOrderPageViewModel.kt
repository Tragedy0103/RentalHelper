package com.example.RentHelper.ViewModels.IType

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.net.toUri
import com.example.RentHelper.Activity.OrderActivity
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.OrderListModel
import com.facebook.drawee.view.SimpleDraweeView

class MyOrderPageViewModel(val str: String) : IType {
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<OrderListItemViewModel>(
                LayoutInflater.from(it.context).inflate(R.layout.item_list_order, it, false)
            ) {
                override fun bind(item: OrderListItemViewModel) {
                    itemView.findViewById<SimpleDraweeView>(R.id.imgProduct)
                        .setImageURI(item.uri, itemView.context)
                    itemView.findViewById<TextView>(R.id.tvTitle).text = item.title
                    itemView.findViewById<TextView>(R.id.tvType).text = item.tradeMethod
                    itemView.findViewById<TextView>(R.id.tvAmount).text =
                        item.tradeQuantity.toString()
                    itemView.findViewById<TextView>(R.id.tvState).text = item.state
                    itemView.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString("orderId", item.orderId)
                        bundle.putString("userId", item.userId)
                        Router.router(
                            Router.NextTmp(
                                itemView.context,
                                OrderActivity::class.java,
                                bundle
                            )
                        )
                    }
                }
            }
        }, ViewType.OrderListItemViewModel)
        .build()
    var items = object : ObserverSomethings<OrderListModel>() {
        override fun update(data: OrderListModel) {
            adapter.clear()
            data.orders.forEach {
                if (it.product?.pics?.size == 0) {
                    adapter.add(
                        OrderListItemViewModel(
                            null,
                            it.orderId,
                            it.lenderId,
                            it.product?.title!!,
                            it.tradeQuantity,
                            it.status,
                            it.getMyTradeMethod()
                        )
                    )
                } else {
                    adapter.add(
                        OrderListItemViewModel(
                            it.product?.pics?.get(0)?.toUri(),
                            it.orderId,
                            it.product?.userId!!,
                            it.product?.title!!,
                            it.tradeQuantity,
                            it.status,
                            it.getMyTradeMethod()
                        )
                    )
                }

            }
        }
    }
    fun getSaleProductList() {
        items.close()
        AccountModel.INSTANCE.token?.let {
            items.onBind(OrderListModel().getSellOrders(it, str))
        }
    }
    fun getBuyProductList() {
        items.close()
        AccountModel.INSTANCE.token?.let {
            items.onBind(OrderListModel().getBuyOrders(it, str))
        }
    }

    override fun getType(): String {
        return ViewType.MyOrderPageViewModel
    }
}