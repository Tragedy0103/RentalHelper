package com.example.RentHelper.ViewModels

import android.util.Log
import android.view.LayoutInflater
import androidx.core.net.toUri
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewModels.IType.ImgViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.OrderModel
import com.facebook.drawee.view.SimpleDraweeView

class OrderProductFragmentViewModel(val orderId: String) : Observed<OrderModel>() {
    var order = OrderModel()
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object :BaseViewHolder<ImgViewModel>(LayoutInflater.from(it.context).inflate(R.layout.item_list_product_image, it, false)){
                override fun bind(item: ImgViewModel) {
                    itemView.findViewById<SimpleDraweeView>(R.id.imgProductItem).setImageURI(item.uri,itemView.context)
                }
            }
        }, ViewType.ImgViewModel).build()
    private val orderObserver = object : ObserverSomethings<OrderModel>() {
        override fun update(data: OrderModel) {
            Log.d("updata",data.orderId)
            data.product?.pics?.forEach {
                adapter.add(ImgViewModel(null,it.toUri()))
            }
        }
    }
    init {
        AccountModel.INSTANCE.token?.let {
            orderObserver.onBind(order)
            order.getById(it, orderId)
        }
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {

    }
}