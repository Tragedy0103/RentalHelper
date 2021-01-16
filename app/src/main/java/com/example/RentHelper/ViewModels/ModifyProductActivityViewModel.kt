package com.example.RentHelper.ViewModels

import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.net.toUri
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewHolder.ImgFooterViewHolder
import com.example.RentHelper.ViewModels.IType.ImgViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.ProductModel
import com.facebook.drawee.view.SimpleDraweeView
import java.io.ByteArrayOutputStream


class ModifyProductActivityViewModel(val productId: String) :
    Observed<ModifyProductActivityViewModel>() {
    class ChangeObserved(isRent:Boolean,isSale: Boolean,isExchange: Boolean):Observed<ChangeObserved>(){
        var isRent:Boolean = isRent
            set(value) {
                field = value
                notify(this)
            }
        var isSale:Boolean = isSale
            set(value) {
                field = value
                notify(this)
            }
        var isExchange:Boolean = isExchange
            set(value) {
                field = value
                notify(this)
            }
        override fun onRegister() {

        }

        override fun onUnRegister() {

        }

        override fun onCloseObserved() {

        }
    }
    var productObserver = object : ObserverSomethings<ProductModel>() {
        override fun update(data: ProductModel) {
            setInfo(
                data.pics,
                data.title,
                data.description,
                data.isSale,
                data.isRent,
                data.isExchange,
                data.price,
                data.deposit,
                data.rent,
                data.weightPrice,
                data.rentMethod,
                data.address,
                data.amount,
                data.type,
                data.type1,
                data.type2
            )
        }
    }
    var title: String? = null
    var description: String? = null
    var isSale: Boolean = false
    var isRent: Boolean = false
    var isExchange: Boolean = false
    var salePrice: Int? = null
    var deposit: Int? = null
    var rent: Int? = null
    var weightPrice: Double? = null
    var rentMethod: String? = null
    var address: String? = null
    var amount: Int? = null
    var Tp1: String? = null
    var Tp2: String? = null
    var Tp3: String? = null
    var Type1: ArrayList<String>? = ArrayList()
    var Type2: ArrayList<String>? = ArrayList()
    var Type3: ArrayList<String>? = ArrayList()

    var changeObserved=ChangeObserved(isSale,isSale, isExchange)
    var imgFooterAction: (() -> Unit)? = null
    var ImgAdapter: CommonAdapter? = CommonAdapter.Builder(ArrayList())
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
    init {
        AccountModel.INSTANCE.token?.let {
            productObserver.onBind(ProductModel().getOwnProductById(it,productId))
        }
    }
    fun removeImg(position:Int){
        ImgAdapter?.remove(position)
    }
    fun setInfo(
        pics :ArrayList<String>,
        title: String,
        description: String,
        isSale: Boolean,
        isRent: Boolean,
        isExchange: Boolean,
        salePrice: Int,
        deposit: Int,
        rent: Int,
        weightPrice: Double,
        rentMethod: String,
        address: String,
        amount: Int,
        Tp1: String,
        Tp2: String,
        Tp3: String
    ) {
        ImgAdapter?.clear()
        if(pics.size!=0){
            pics.forEach {
                addImg(ImgViewModel(null,it.toUri()))
            }
        }
        this.title = title
        this.description = description
        this.isSale = isSale
        this.isRent = isRent
        this.isExchange = isExchange
        this.salePrice = salePrice
        this.deposit = deposit
        this.rent = rent
        this.weightPrice = weightPrice
        this.rentMethod = rentMethod
        this.address = address
        this.amount = amount
        this.Tp1 = Tp1
        this.Tp2 = Tp2
        this.Tp3 = Tp3
        notify(this)
    }

    fun addImg(imgViewModel: ImgViewModel) {
        ImgAdapter?.add(imgViewModel)
    }


    fun getType1Position():Int{
        var tmp   = 0
        Type1?.forEach {
            if (Tp1!=it){
                tmp++
            }else{
                return tmp
            }
        }
        return tmp
    }
    fun getType2Position():Int{
        var tmp   = 0
        Type2?.forEach {
            if (Tp2!=it){
                tmp++
            }else{
                return tmp
            }
        }
        return tmp
    }
    fun getType3Position():Int{
        var tmp   = 0
        Type3?.forEach {
            if (Tp3!=it){
                tmp++
            }else{
                return tmp
            }
        }
        return tmp
    }
    fun createType1(): ArrayList<String> {
        Type1?.clear()
        Type1?.add("PlayStation")
        Type1?.add("XBox")
        Type1?.add("Switch")
        Type1?.add("桌遊")
        Type1?.add("Other")
        return Type1!!
    }

    fun createType2(key: String): ArrayList<String> {
        Type2?.clear()
        when (key) {
            "PlayStation" -> {
                Type2?.add("PS5")
                Type2?.add("PS4")
            }
            "XBox" -> {
                Type2?.add("Series")
                Type2?.add("One")
            }
            "Switch" -> {
                Type2?.add("Switch")
            }
            "桌遊" -> {
                Type2?.add("4人以下")
                Type2?.add("4到8人")
                Type2?.add("8人以上")
            }
            "Other" -> {
                Type2?.add("其他主機")
                Type2?.add("自製遊戲")
            }
        }
        return Type2!!
    }

    fun createType3(key: String): ArrayList<String> {
        Type3?.clear()
        when (key) {
            "Other" -> {
                Type3?.add("其他")
            }
            "桌遊" -> {
                Type3?.add("策略")
                Type3?.add("友情破壞")
                Type3?.add("技巧")
                Type3?.add("經營")
                Type3?.add("運氣")
                Type3?.add("劇情")
                Type3?.add("TRPG")
                Type3?.add("其他")
            }
            else -> {
                Type3?.add("遊戲")
                Type3?.add("主機")
                Type3?.add("周邊")
                Type3?.add("其他")
            }
        }
        return Type3!!
    }

    fun modifyProduct(
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
        onSuccess: (str: String) -> Unit,
        onFail: (str: String) -> Unit
    ): ProductModel? {
        var product: ProductModel? = null
        var tmpWeightPrice = weightPrice
        var tmpRent = rent
        var tmpSalePrice = salePrice
        if (!isExchange && !isRent && !isSale) {
            onFail.invoke("請選擇上架模式")
            return null
        }
        if (title == "") {
            onFail.invoke("請輸入產品名稱")
            return null
        }
        if (rentMethod == "") {
            onFail.invoke("請輸入交易方式")
            return null
        }
        if (!isExchange) {
            tmpWeightPrice = 0.0
        }
        if (!isRent) {
            tmpRent = 0
        }
        if (!isSale) {
            tmpSalePrice = 0
        }
        AccountModel.INSTANCE.token?.let {
            product = ProductModel().modifyProduct(
                it,
                productId,
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

    fun getPics(): ArrayList<String> {
        val tmp = ArrayList<String>()
        ImgAdapter?.map { itype ->
            if (itype is ImgViewModel) {

                if(itype.bitmap !=null ){
                    val outputStream = ByteArrayOutputStream()
                    itype.bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    tmp.add(Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT))
                }else {
                    tmp.add(itype.uri.toString())
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
        ImgAdapter = null
        Type1 = null
        Type2 = null
        Type3 = null
    }
}