package com.example.RentHelper.models

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.net.toUri
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ProductModel() : Observed<ProductModel>() {
    var id: String = ""
    var pics: ArrayList<String> = ArrayList()
    var title: String = ""
    var description: String = ""
    var isSale: Boolean = true
    var isRent: Boolean = false
    var isExchange: Boolean = false
    var deposit: Int = 0
    var address: String = ""
    var rent: Int = 0
    var price: Int = 0
    var rentMethod: String = ""
    var amount: Int = 0
    var type: String = ""
    var type1: String = ""
    var type2: String = ""
    var userId: String = ""
    var weightPrice: Double = 0.0

    constructor(
        id: String?,
        pics: ArrayList<String>,
        title: String,
        description: String,
        isSale: Boolean,
        isRent: Boolean,
        isExchange: Boolean,
        deposit: Int,
        address: String,
        rent: Int,
        price: Int,
        rentMethod: String,
        amount: Int,
        type: String,
        type1: String,
        type2: String,
        userId: String,
        weightPrice: Double
    ) : this() {
        id?.let { this.id = it }
        this.pics.addAll(pics)
        this.title = title
        this.description = description
        this.isSale = isSale
        this.isRent = isRent
        this.isExchange = isExchange
        this.deposit = deposit
        this.address = address
        this.rent = rent
        this.price = price
        this.rentMethod = rentMethod
        this.amount = amount
        this.type = type
        this.type1 = type1
        this.type2 = type2
        this.userId = userId
        this.weightPrice = weightPrice
    }

    companion object {
        fun picsToString(jsonArray: JSONArray): ArrayList<String> {
            val tmp = ArrayList<String>()
            for (i in 0 until jsonArray.length()) {
                tmp.add(jsonArray.getJSONObject(i).getString("path"))
            }
            return tmp
        }

        fun stringToPics(strings: ArrayList<String>): JSONArray {
            val tmp = JSONArray()
            strings.forEach {
                tmp.put(
                    JSONObject()
                        .put("Path", it)
                )
            }
            return tmp
        }

        fun addProduct(
            token: String,
            title: String,
            description: String,
            address: String,
            isSale: Boolean,
            isRent: Boolean,
            isExchange: Boolean,
            deposit: Int,
            rent: Int,
            price: Int,
            rentMethod: String,
            amount: Int,
            type: String,
            type1: String,
            type2: String,
            pics: ArrayList<String>,
            weightPrice: Double,
            onSuccess: (str: String) -> Unit,
            onFail: (str: String) -> Unit
        ): ProductModel {
            val productModel = ProductModel(
                null,
                pics,
                title,
                description,
                isSale,
                isRent,
                isExchange,
                deposit,
                address,
                rent,
                price,
                rentMethod,
                amount,
                type,
                type1,
                type2,
                AccountModel.INSTANCE.id,
                weightPrice
            )
            NetworkController.instance.addProduct(
                token,
                title,
                description,
                address,
                isSale,
                isRent,
                isExchange,
                deposit,
                rent,
                price,
                rentMethod,
                amount,
                type,
                type1,
                type2,
                stringToPics(pics),
                weightPrice
            )
                .onResponse { code, res ->
                    productModel.id = res
                    Handler(Looper.getMainLooper()).post {
                        when (code ) {
                            200->{
                                onSuccess.invoke("新增成功")
                                productModel.notify(productModel)
                            }
                            else ->{
                                onFail.invoke("產品新增失敗")
                            }
                        }
                    }
                }
                .onFailure {
                    Log.d("addProduct", "fail")
                }
                .exec()
            return productModel
        }

        fun addCart(
            token: String, id: String,
            onSuccess: (str: String) -> Unit,
            onFail: (str: String) -> Unit
        ) {
            NetworkController.instance.addCartItem(token, id)
                .onResponse { code, res ->
                    Handler(Looper.getMainLooper()).post {
                        when (code) {
                            200 -> {
                                onSuccess.invoke("新增成功")
                            }
                            400 -> {
                                onFail.invoke("重複添加")
                            }
                            else -> {
                                onFail.invoke("新增失敗")
                            }
                        }
                    }
                }
                .onFailure {
                }
                .exec()
        }

        fun deleteCart(
            token: String, id: String,
            onSuccess: (str: String) -> Unit,
            onFail: (str: String) -> Unit
        ) {
            NetworkController.instance.deleteCartItem(token, id)
                .onResponse { code, res ->
                    Handler(Looper.getMainLooper()).post {
                        when (code) {
                            200 -> {
                                onSuccess.invoke("刪除成功")
                            }
                            else -> {
                                onFail.invoke("刪除失敗")
                            }
                        }
                    }
                }
                .exec()
        }

        fun delete(
            token: String, id: String,
            onSuccess: (str: String) -> Unit,
            onFail: (str: String) -> Unit
        ) {
            NetworkController.instance.deleteProduct(token, id)
                .onResponse { code, res ->
                    Handler(Looper.getMainLooper()).post {
                        when (code) {
                            200 -> {
                                onSuccess.invoke("刪除成功")
                            }
                            else -> {
                                onFail.invoke("刪除失敗")
                            }
                        }
                    }
                }
                .exec()
        }


    }

    fun setInfo(jsonObject: JSONObject) {
        this.id = jsonObject.getString("id")
        this.pics.addAll(picsToString(jsonObject.getJSONArray("pics")))
        this.title = jsonObject.getString("title")
        this.description = jsonObject.getString("description")
        this.isSale = jsonObject.getBoolean("isSale")
        this.isRent = jsonObject.getBoolean("isRent")
        this.isExchange = jsonObject.getBoolean("isExchange")
        this.deposit = jsonObject.getInt("deposit")
        this.address = jsonObject.getString("address")
        this.rent = jsonObject.getInt("rent")
        this.price = jsonObject.getInt("salePrice")
        this.rentMethod = jsonObject.getString("rentMethod")
        this.amount = jsonObject.getInt("amount")
        this.type = jsonObject.getString("type")
        this.type1 = jsonObject.getString("type1")
        this.type2 = jsonObject.getString("type2")
        this.userId = jsonObject.getString("userId")
        this.weightPrice = jsonObject.getDouble("weightPrice")
        notify(this)
    }

    fun getMyType(): ArrayList<String> {
        val tmp = ArrayList<String>()
        tmp.add(type)
        tmp.add(type1)
        tmp.add(type2)
        return tmp
    }
    fun getMySaleType(): ArrayList<String> {
        val tmp = ArrayList<String>()
        if (isSale) {
            tmp.add("出售")
        }
        if (isRent) {
            tmp.add("出租")
        }
        if (isExchange) {
            tmp.add("交換")
        }
        return tmp
    }

    fun getFirstImg(): Uri? {
        if (pics.isNotEmpty()) {
            return pics[0].toUri()
        }
        return null

    }

    fun getPrice(): String {
        var tmpprice = ""
        if (isSale) {
            tmpprice += "\n" +
                    "定價：" + price
        }
        if (isRent) {
            tmpprice += "\n" +
                    "租金：" + rent + "\n" +
                    "保證金：" + deposit
        }
        if (isExchange) {
            tmpprice += "\n" +
                    "權重：" + weightPrice
        }
        return tmpprice
    }

    fun getOwnProductById(token: String, productId: String): ProductModel {
        NetworkController.instance.getOwnProductById(token, productId)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonObject = JSONObject(res)
                            this.setInfo(jsonObject)
                            notify(this)
                        }
                    }
                }
            }
            .onFailure {
                Log.d("Product", it.message!!)
            }
            .exec()
        return this

    }

    fun getById(id: String): ProductModel {
        NetworkController.instance.getProductInfoById(id)
            .onResponse { _, res ->
                try {
                    val jsonObject = JSONObject(res)
                    Handler(Looper.getMainLooper()).post {
                        this.setInfo(jsonObject)
                        notify(this)
                    }
                } catch (e: JSONException) {
                }
            }
            .onFailure {
                Log.d("Product", it.message!!)
            }
            .onComplete {

            }
            .exec()
        return this
    }


    fun modifyProduct(
        token: String?,
        id: String,
        title: String?,
        description: String?,
        address: String?,
        isSale: Boolean?,
        isRent: Boolean?,
        isExchange: Boolean?,
        deposit: Int?,
        rent: Int?,
        saleprice: Int?,
        rentMethod: String?,
        amount: Int?,
        type: String?,
        type1: String?,
        type2: String?,
        pics: ArrayList<String>,
        weightPrice: Double?,
        onSuccess: (str: String) -> Unit,
        onFail: (str: String) -> Unit
    ): ProductModel {
        NetworkController.instance.modifyProduct(
            token,
            id,
            title,
            description,
            address,
            isSale,
            isRent,
            isExchange,
            deposit,
            rent,
            saleprice,
            rentMethod,
            amount,
            type,
            type1,
            type2,
            stringToPics(pics),
            weightPrice
        ).onResponse { code, res ->
            when (code) {
                200 -> {
                    title?.let { this.title = title }
                    description?.let { this.description = description }
                    address?.let { this.address = address }
                    isSale?.let { this.isSale = isSale }
                    isRent?.let { this.isRent = isRent }
                    isExchange?.let { this.isExchange = isExchange }
                    deposit?.let { this.deposit = deposit }
                    rent?.let { this.rent = rent }
                    saleprice?.let { this.price = saleprice }
                    rentMethod?.let { this.rentMethod = rentMethod }
                    amount?.let { this.amount = amount }
                    type?.let { this.type = type }
                    type1?.let { this.type1 = type1 }
                    type2?.let { this.type2 = type2 }
                    pics?.let { this.pics = pics }
                    weightPrice?.let { this.weightPrice = weightPrice }
                    Handler(Looper.getMainLooper()).post {
                        onSuccess.invoke("修改成功")
                        notify(this)
                    }
                }
                else -> {
                    Log.d("modifyFail", "" + code + res)
                    Handler(Looper.getMainLooper()).post {
                        onFail.invoke("修改失敗")
                    }

                }
            }

        }
            .exec()
        return this
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
    }

}
