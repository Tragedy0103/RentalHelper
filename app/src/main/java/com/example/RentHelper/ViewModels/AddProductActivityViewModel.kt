package com.example.RentHelper.ViewModels

import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewHolder.ImgFooterViewHolder
import com.example.RentHelper.ViewModels.IType.ImgViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.ProductModel
import com.facebook.drawee.view.SimpleDraweeView
import java.io.ByteArrayOutputStream


class AddProductActivityViewModel : Observed<AddProductActivityViewModel>() {
    companion object{
        val instances:AddProductActivityViewModel by lazy { AddProductActivityViewModel() }
    }
    var type1: ArrayList<String> = ArrayList()
    var Type2: ArrayList<String> = ArrayList()
    var Type3: ArrayList<String> = ArrayList()


    var isRent: Boolean = false
        set(value) {
            field = value
            notify(this)
        }
    var isSale: Boolean = false
        set(value) {
            field = value
            notify(this)
        }
    var isChange: Boolean = false
        set(value) {
            field = value
            notify(this)
        }
    var imgFooterAction: (() -> Unit)? = null
    val ImgAdapter: CommonAdapter = CommonAdapter.Builder(ArrayList())
        .addFooter { viewGroup ->
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_list_addproduct_add, viewGroup, false)
            view.setOnClickListener {
                imgFooterAction?.invoke()
            }
            ImgFooterViewHolder(view)
        }
        .addType({ viewGroup ->
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_list_addproduct_img, viewGroup, false)
            object :BaseViewHolder<ImgViewModel>(view){
                override fun bind(item: ImgViewModel) {
                    itemView.findViewById<SimpleDraweeView>(R.id.sdvImg).setImageURI(item.uri,itemView.context)
                    itemView.findViewById<ImageView>(R.id.imgDelete).setOnClickListener {
                        removeImg(adapterPosition)
                    }
                }
            }
        }, ViewType.ImgViewModel)
        .build()
    fun removeImg(position:Int){
        ImgAdapter.remove(position)
    }

    fun addImg(imgViewModel: ImgViewModel) {
        ImgAdapter.add(imgViewModel)
    }


    fun createType1(): ArrayList<String> {
        type1.clear()
        type1.add("PlayStation")
        type1.add("XBox")
        type1.add("Switch")
        type1.add("桌遊")
        type1.add("Other")
        return type1
    }

    fun createType2(key: String): ArrayList<String> {
        Type2.clear()
        when (key) {
            "PlayStation" -> {
                Type2.add("PS5")
                Type2.add("PS4")
            }
            "XBox" -> {
                Type2.add("Series")
                Type2.add("One")
            }
            "Switch" -> {
                Type2.add("Switch")
            }
            "桌遊" -> {
                Type2.add("4人以下")
                Type2.add("4到8人")
                Type2.add("8人以上")
            }
            "Other" -> {
                Type2.add("其他主機")
                Type2.add("自製遊戲")
            }
        }
        return Type2
    }

    fun createType3(key: String): ArrayList<String> {
        Type3.clear()
        when (key) {
            "Other" -> {
                Type3.add("其他")
            }
            "桌遊" -> {
                Type3.add("策略")
                Type3.add("友情破壞")
                Type3.add("技巧")
                Type3.add("經營")
                Type3.add("運氣")
                Type3.add("劇情")
                Type3.add("TRPG")
                Type3.add("其他")
            }
            else -> {
                Type3.add("遊戲")
                Type3.add("主機")
                Type3.add("周邊")
                Type3.add("其他")
            }
        }
        return Type3
    }

    fun addProduct(
        title: String,
        description: String,
        isSale: Boolean,
        isRent: Boolean,
        isExchange: Boolean,
        deposit: Int,
        address: String,
        rent: Int,
        salePrice: Int,
        weightPrice: Double,
        rentMethod: String,
        amount: Int,
        type: String,
        type1: String,
        type2: String,
        onSuccess:(str:String)->Unit,
        onFail:(str:String)->Unit
    ): ProductModel? {
        var product: ProductModel? =null
        var tmpWeightPrice=weightPrice
        var tmpRent =rent
        var tmpSalePrice = salePrice
        if(!isExchange&&!isRent&&!isSale){
            onFail.invoke("請選擇上架模式")
            return null
        }
        if (title == ""){
            onFail.invoke("請輸入產品名稱")
            return null
        }
        if(rentMethod == ""){
            onFail.invoke("請輸入交易方式")
            return null
        }
        if(!isExchange){ tmpWeightPrice = 0.0}
        if(!isRent){ tmpRent = 0}
        if(!isSale){ tmpSalePrice = 0}
        AccountModel.INSTANCE.token?.let {
            product = ProductModel.addProduct(
                it,
                title,
                description,
                address,
                isSale,
                isRent,
                isExchange,
                deposit,
                tmpRent,
                tmpSalePrice,
                rentMethod,
                amount,
                type,
                type1,
                type2,
                getPics(),
                tmpWeightPrice,
                onSuccess,
                onFail
            )
        }
        return product
    }
    fun getPics():ArrayList<String>{
        val tmp =ArrayList<String>()
        ImgAdapter.map { itype->
            if(itype is ImgViewModel){
                itype.bitmap?.let {
                    val outputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    tmp.add(Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT))
                }
            }
        }
        return tmp
    }
    override fun onRegister() {
    }

    override fun onUnRegister() {
    }

    override fun onCloseObserved() {
        imgFooterAction = null
        ImgAdapter.clear()
        isRent =false
        isSale=false
        isChange=false
        type1.clear()
        Type2.clear()
        Type3.clear()
    }
}