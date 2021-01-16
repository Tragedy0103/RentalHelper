package com.example.RentHelper.Controller

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class NetworkController {
    val url = "http://35.221.140.217/api/"
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    val client: OkHttpClient = OkHttpClient()

    companion object {
        val instance: NetworkController by lazy { NetworkController() }
    }


    inner class CallbackMiddle(val request: Request) {
        private var onFailure: ((e: IOException) -> Unit)? = null
        private var onResponse: ((code: Int, res: String) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        fun onFailure(onFailure: (e: IOException) -> Unit): CallbackMiddle {
            this.onFailure = onFailure
            return this
        }

        fun onResponse(onResponse: (code: Int, res: String) -> Unit): CallbackMiddle {
            this.onResponse = onResponse
            return this
        }

        fun onComplete(onComplete: () -> Unit): CallbackMiddle {
            this.onComplete = onComplete
            return this
        }

        fun exec() {
            client.newCall(request).enqueue(
                CallbackAdaptor(
                    onFailure,
                    onResponse,
                    onComplete
                )
            )
        }
    }

    class CallbackAdaptor(
        val onFailure: ((e: IOException) -> Unit)?,
        val onResponse: ((code: Int, res: String) -> Unit)?,
        val onCompete: (() -> Unit)?
    ) : Callback {
        override fun onFailure(call: Call, e: IOException) {

            onFailure?.invoke(e)
            onComplete()
        }

        override fun onResponse(call: Call, response: Response) {
            val res = response.body?.string()
            val code = response.code
            onResponse?.invoke(code, res!!)
            onComplete()
        }

        private fun onComplete() {
            onCompete?.invoke()
        }
    }

    fun login(account: String, password: String,deviceToken:String): CallbackMiddle {
        Log.d("dtoken","token: "+deviceToken)
        val requestBody: RequestBody = JSONObject()
            .put("Email", account)
            .put("Password", password)
            .put("DeviceToken",deviceToken)
            .toString()
            .toRequestBody(JSON)
        val request = Request.Builder()
            .url(url + "Users/login")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
    }

    fun register(
        email: String,
        verityCode: String,
        password: String,
        name: String,
        phone: String
    ): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("Email", email)
            .put("VerityCode", verityCode)
            .put("Password", password)
            .put("Name", name)
            .put("Phone", phone)
            .toString()
            .toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url + "Users/register")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
    }

    fun modify(email: String, verityCode: String, password: String): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("Email", email)
            .put("VerityCode", verityCode)
            .put("NewPassword", password)
            .toString()
            .toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url + "Users/security/vp")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
    }


    fun sendVerityCode(email: String): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("Email", email)
            .toString()
            .toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url + "Users/security/check")
            .post(requestBody)
            .build()

        return CallbackMiddle(request)
    }

    fun userInfo(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Users/info")
            .header("Authorization", "bearer $token")
            .build()
        return CallbackMiddle(request)
    }

    fun userBasicInfo(userId: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Users/baseInfo/" + userId)
            .build()
        return CallbackMiddle(request)
    }

    fun userInfoModify(
        token: String,
        name: String,
        nickName: String,
        phone: String,
        address: String
    ): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("name", name)
            .put("nickName", nickName)
            .put("phone", phone)
            .put("address", address)
            .toString()
            .toRequestBody(JSON)
        val request = Request.Builder()
            .url(url + "Users/info")
            .header("Authorization", "bearer $token")
            .patch(requestBody)
            .build()
        return CallbackMiddle(request)
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
        saleprice: Int,
        rentMethod: String,
        amount: Int,
        type: String,
        type1: String,
        type2: String,
        pics: JSONArray,
        weightPrice: Double
    ): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("title", title)
            .put("description", description)
            .put("address", address)
            .put("isSale", isSale)
            .put("isRent", isRent)
            .put("isExchange", isExchange)
            .put("deposit", deposit)
            .put("rent", rent)
            .put("saleprice", saleprice)
            .put("rentMethod", rentMethod)
            .put("amount", amount)
            .put("type", type)
            .put("type1", type1)
            .put("type2", type2)
            .put("pics", pics)
            .put("weightPrice", weightPrice)
            .toString()
            .toRequestBody(JSON)
        val request = Request.Builder()
            .url(url + "Products/add")
            .header("Authorization", "bearer $token")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
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
        pics: JSONArray?,
        weightPrice: Double?
    ): CallbackMiddle {
        val jsonObject = JSONObject()
        jsonObject.put("Id", id)
        title?.let { jsonObject.put("title", title) }
        description?.let { jsonObject.put("description", description) }
        address?.let { jsonObject.put("address", address) }
        isSale?.let { jsonObject.put("isSale", isSale) }
        isRent?.let { jsonObject.put("isRent", isRent) }
        isExchange?.let { jsonObject.put("isExchange", isExchange) }
        deposit?.let { jsonObject.put("deposit", deposit) }
        rent?.let { jsonObject.put("rent", rent) }
        saleprice?.let { jsonObject.put("saleprice", saleprice) }
        rentMethod?.let { jsonObject.put("rentMethod", rentMethod) }
        amount?.let { jsonObject.put("amount", amount) }
        type?.let { jsonObject.put("type", type) }
        type1?.let { jsonObject.put("type1", type1) }
        type2?.let { jsonObject.put("type2", type2) }
        pics?.let { jsonObject.put("pics", pics) }
        weightPrice?.let { jsonObject.put("weightPrice", weightPrice) }
        val requestBody: RequestBody = jsonObject
            .toString()
            .toRequestBody(JSON)

        val request = Request.Builder()
            .url(url + "Products/modify")
            .header("Authorization", "bearer $token")
            .patch(requestBody)
            .build()
        return CallbackMiddle(request)
    }

    fun deleteProduct(token: String, id: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/ownItem/$id")
            .header("Authorization", "bearer $token")
            .delete()
            .build()
        return CallbackMiddle(request)

    }

    fun getProductOnShelf(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/ownItemOnShelf")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getProductNotOnShelf(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/ownItemNotOnShelf")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }
    fun getOwnProductById(token:String,productId: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/ownItem/$productId")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }
    fun getProductInfoById(productId: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/ById/" + productId)
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getProductListByType(typeName: String, index: Int, amount: Int): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listByType/$typeName/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getProductListByType1(typeName: String, index: Int, amount: Int): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listByType1/$typeName/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getProductListByType2(
        typeName: String,
        typeName2: String,
        index: Int,
        amount: Int
    ): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listByType2/$typeName/$typeName2/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }
    fun getProductListByType3(
        typeName: String,
        typeName2: String,
        index: Int,
        amount: Int
    ): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listByNType2/$typeName/$typeName2/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }
    fun getProductList(
        typeName: String,
        typeName2: String,
        typeName3: String,
        index: Int,
        amount: Int
    ): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listBy/$typeName/$typeName2/$typeName3/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getProductListByKeyWork(
        typeName: String,
        typeName2: String,
        keyWork: String,
        index: Int,
        amount: Int
    ): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listByType1/$typeName/$typeName2/$keyWork/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getProductListBySellerId(id: String, index: Int, amount: Int): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Products/listBySeller/$id/$index/$amount")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun addOrder(
        token: String,
        productId: String,
        tradeMethod: Int,
        TradeItem: JSONArray,
        amount: Int
    ): CallbackMiddle {
        val jsonObject = JSONObject()
            .put("ProductId", productId)
            .put("TradeMethod", tradeMethod)
            .put("TradeQuantity", amount)
            .put("OrderExchangeItems", TradeItem)
        val requestBody: RequestBody = JSONObject()
            .put("ProductId", productId)
            .put("TradeMethod", tradeMethod)
            .put("OrderExchangeItems", TradeItem)
            .put("TradeQuantity", amount)
            .toString()
            .toRequestBody(JSON)
        Log.d("requestBody", jsonObject.toString())
        val request = Request.Builder()
            .url(url + "Orders/add")
            .header("Authorization", "bearer $token")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)

    }

    fun getOrderById(token: String, orderId: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Orders/$orderId")
            .header("Authorization", "bearer $token")
            .build()
        return CallbackMiddle(request)
    }

    fun getOwnOrder(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Orders/Mylist")
            .header("Authorization", "bearer $token")
            .build()
        return CallbackMiddle(request)
    }

    fun getSellOrder(token: String,status: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Orders/Mylist/seller/$status")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getBuyOrder(token: String,status:String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Orders/Mylist/buyer/$status")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }
    fun  orderStatusChange(token: String,orderId:String,currentStatus:String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Orders/status/$orderId/$currentStatus")
            .header("Authorization", "bearer $token")
            .patch( JSONObject().toString().toRequestBody())
            .build()
        return CallbackMiddle(request)
    }
    fun addWishItem(
        token: String,
        ExchangeItem: String,
        RequestQuantity: Int,
        WeightPoint: Double
    ): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("ExchangeItem", ExchangeItem)
            .put("RequestQuantity", RequestQuantity)
            .put("WeightPoint", WeightPoint)
            .toString()
            .toRequestBody(JSON)
        val request = Request.Builder()
            .url(url + "Users/wishlist/new")
            .header("Authorization", "bearer $token")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
    }

    fun getWishList(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Users/wishlist/All")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun getWishListOnShelf(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Users/wishlist/onshelf")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun deleteWishItem(token: String, ItemId: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Users/wishlist/$ItemId")
            .header("Authorization", "bearer $token")
            .delete()
            .build()
        return CallbackMiddle(request)
    }

    fun addNote(token: String, orderId: String, message: String): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("OrderId", orderId)
            .put("Message", message)
            .toString()
            .toRequestBody(JSON)
        val request = Request.Builder()
            .url(url + "Notes/add")
            .header("Authorization", "bearer $token")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
    }

    fun getNoteList(token: String, orderId: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "Notes/listBy/$orderId")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun addCartItem(token: String,id: String): CallbackMiddle {
        val requestBody: RequestBody = JSONObject()
            .put("ProductId", id)
            .toString()
            .toRequestBody(JSON)
        val request = Request.Builder()
            .url(url + "CartItems/add")
            .header("Authorization", "bearer $token")
            .post(requestBody)
            .build()
        return CallbackMiddle(request)
    }

    fun getCartList(token: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "CartItems/productList")
            .header("Authorization", "bearer $token")
            .get()
            .build()
        return CallbackMiddle(request)
    }

    fun deleteCartItem( token: String,id: String): CallbackMiddle {
        val request = Request.Builder()
            .url(url + "CartItems/$id")
            .header("Authorization", "bearer $token")
            .delete()
            .build()
        return CallbackMiddle(request)
    }
}