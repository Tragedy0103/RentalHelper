package com.example.RentHelper.ViewModels.IType

import android.net.Uri
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class OrderListItemViewModel(
    val uri: Uri?,
    val orderId: String,
    val userId: String,
    val title: String,
    val tradeQuantity: Int,
    val state: String,
    val tradeMethod: String
):IType {
    override fun getType(): String {
        return ViewType.OrderListItemViewModel
    }
}