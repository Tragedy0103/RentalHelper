package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.ViewModels.IType.TrideItemsViewModel
import org.json.JSONArray
import org.json.JSONObject

class OrderModel() : Observed<OrderModel>() {
    var product: ProductModel? = null
    var lender: UserModel? = null
    var orderId: String = ""
    var productId: String = ""
    var lenderId: String = ""
    var tradeMethod: Int? = 0
    var orderExchangeItems: ArrayList<OrderExchangeItemModel> = ArrayList()
    var tradeQuantity: Int = 0
    var status: String = ""
    var orderTime: String = ""
    var payTime: String = ""
    var productSend: String = ""
    var productArrive: String = ""
    var productSendBack: String = ""
    var productGetBack: String = ""
    var notes: ArrayList<NoteModel> = ArrayList()

    companion object {
        fun getExchangeItemList(jsonArray: JSONArray?): ArrayList<OrderExchangeItemModel> {
            val tmp = ArrayList<OrderExchangeItemModel>()
            if (jsonArray == null) {
                return tmp
            }
            for (i in 0 until jsonArray.length()) {
                tmp.add(
                    OrderExchangeItemModel(
                        jsonArray.getJSONObject(i).getString("id"),
                        jsonArray.getJSONObject(i).getString("orderId"),
                        jsonArray.getJSONObject(i).getString("wishItemId"),
                        jsonArray.getJSONObject(i).getString("exchangeItem"),
                        jsonArray.getJSONObject(i).getString("packageQuantity")
                    )
                )
            }
            return tmp
        }

    }

    fun ChangeStatus(
        token: String,
        onSuccess: ((str: String) -> Unit)?,
        onFail: ((str: String) -> Unit)?
    ) {
        NetworkController.instance.orderStatusChange(token, orderId, status)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            onSuccess?.invoke("切換成功")
                        }
                        400 -> {
                            onFail?.invoke("狀態錯誤")
                        }
                        else -> {
                            onFail?.invoke("切換失敗")
                        }
                    }
                }

            }
            .exec()
    }

    fun getMyValue(): String {
        return when (tradeMethod) {

            0 -> {
                var tmp = ""
                product?.let {
                    tmp += "租金：" + it.rent * tradeQuantity + "\n" +
                            "保證金：" + it.deposit * tradeQuantity + "\n"
                }
                tmp
            }
            1 -> {
                var tmp = ""
                product?.let {
                    tmp += "定價：" + it.price * tradeQuantity
                }
                tmp
            }
            2 -> {
                var tmp = ""
                orderExchangeItems.forEach {
                    tmp += it.exchangeItem + "*" + it.packageQuantity + "\n"
                }
                tmp
            }
            else -> ""
        }
    }

    fun getMyCurrentStateDate(): List<String> {
        return when (status) {
            "已立單" -> {
                return orderTime.split("T")
            }
            "買家已完成支付" -> {
                return payTime.split("T")
            }
            "已寄送" -> {
                return productSend.split("T")
            }
            "已抵達" -> {
                return productArrive.split("T")
            }
            "歸還已寄出" -> {
                return productSendBack.split("T")
            }
            "已歸還" -> {
                return productGetBack.split("T")
            }
            else -> ArrayList()
        }
    }

    fun getMyTradeMethod(): String {
        return when (tradeMethod) {
            0 -> "租借"
            1 -> "買賣"
            2 -> "交換"
            else -> ""
        }
    }

    fun getOrder(jsonObject: JSONObject): OrderModel {
        this.orderId = jsonObject.getString("id")
        this.product = ProductModel(
            jsonObject.getString("productId"),
            ProductModel.picsToString(jsonObject.getJSONArray("pics")),
            jsonObject.getString("p_Title"),
            jsonObject.getString("p_Desc"),
            jsonObject.getBoolean("p_isSale"),
            jsonObject.getBoolean("p_isRent"),
            jsonObject.getBoolean("p_isExchange"),
            jsonObject.getInt("p_Deposit"),
            jsonObject.getString("p_Address"),
            jsonObject.getInt("p_Rent"),
            jsonObject.getInt("p_salePrice"),
            jsonObject.getString("p_RentMethod"),
            0,
            jsonObject.getString("p_Type"),
            jsonObject.getString("p_Type1"),
            jsonObject.getString("p_Type2"),
            jsonObject.getString("p_ownerId"),
            jsonObject.getDouble("p_WeightPrice")
        )
        this.lender = UserModel().getInfo(jsonObject.getString("lender"))
        this.orderTime = jsonObject.getString("orderTime")
        this.payTime = jsonObject.getString("payTime")
        this.productSend = jsonObject.getString("productSend")
        this.productArrive = jsonObject.getString("productArrive")
        this.productSendBack = jsonObject.getString("productSendBack")
        this.productGetBack = jsonObject.getString("productGetBack")
        this.status = jsonObject.getString("status")
        this.tradeQuantity = jsonObject.getInt("tradeQuantity")
        this.tradeMethod = jsonObject.getInt("tradeMethod")
        orderExchangeItems.clear()
        this.orderExchangeItems.addAll(getExchangeItemList(jsonObject.getJSONArray("orderExchangeItems")))
        notes.clear()
        this.notes.addAll(NoteModel.toNoteList(jsonObject.getJSONArray("notes")))
        return this
    }

    fun getById(token: String, id: String): OrderModel {
        NetworkController.instance.getOrderById(token, id)
            .onResponse { code, res ->
                when (code) {
                    200 -> {
                        getOrder(JSONObject(res))
                        Handler(Looper.getMainLooper()).post {
                            notify(this)
                        }
                    }
                }
            }
            .onFailure {
            }
            .exec()
        return this
    }

    fun TrideItemsToJSONObject(item: TrideItemsViewModel): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("WishItemId", item.wishItemId)
            .put("packageQuantity", item.currentAmount)
        return jsonObject
    }

    fun TeadeItemToJSONArray(items: ArrayList<TrideItemsViewModel>): JSONArray {
        val jsonArray: JSONArray = JSONArray()
        items.forEach {
            jsonArray.put(TrideItemsToJSONObject(it))
        }
        Log.d("jsonArray", jsonArray.toString())
        return jsonArray
    }

    fun addOrder(
        token: String,
        productId: String,
        tradeMethod: Int,
        TradeItem: ArrayList<TrideItemsViewModel>,
        amount: Int, action: (() -> Unit)?
    ) {
        NetworkController.instance.addOrder(
            token,
            productId,
            tradeMethod,
            TeadeItemToJSONArray(TradeItem),
            amount
        )
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            this.orderId = res
                            action?.invoke()
                        }
                    }

                }

            }
            .exec()
    }

    override fun onRegister() {
    }

    override fun onUnRegister() {
    }

    override fun onCloseObserved() {
    }
}