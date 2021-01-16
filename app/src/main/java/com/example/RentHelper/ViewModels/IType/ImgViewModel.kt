package com.example.RentHelper.ViewModels.IType

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.ViewType

class ImgViewModel(cr : ContentResolver?, val uri: Uri):IType {
    var bitmap:Bitmap?= null
    init {
        cr?.let { bitmap = BitmapFactory.decodeStream(it.openInputStream(uri)) }
    }
    override fun getType(): String {
        return ViewType.ImgViewModel
    }
}