package com.example.RentHelper.ViewModels.IType

import android.net.Uri
import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.Tool.IType

class ProductCardViewModel(
    val id: String,
    val title: String,
    val price: Int,
    var saleType:ArrayList<String>,
    val uri: Uri?
) : IType {
    override fun getType(): String {
        return ViewType.ProductCardViewModel
    }
}