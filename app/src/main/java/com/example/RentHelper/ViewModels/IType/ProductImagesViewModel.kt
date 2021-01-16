package com.example.RentHelper.ViewModels.IType

import androidx.core.net.toUri
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class ProductImagesViewModel(path:String):IType {
    val uri = path.toUri()
    init{

    }
    override fun getType(): String {
        return ViewType.ProductImagesViewModel
    }
}