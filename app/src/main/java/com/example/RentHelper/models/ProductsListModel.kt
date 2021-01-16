package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class ProductsListModel : Observed<ProductsListModel>() {
    var items: ArrayList<ProductModel> = ArrayList()

    companion object {
        fun getProductList(jsonArray: JSONArray): ArrayList<ProductModel> {
            val tmp = ArrayList<ProductModel>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                tmp.add(
                    ProductModel(
                        jsonObject.getString("id"),
                        ProductModel.picsToString(jsonObject.getJSONArray("pics")),
                        jsonObject.getString("title"),
                        jsonObject.getString("description"),
                        jsonObject.getBoolean("isSale"),
                        jsonObject.getBoolean("isRent"),
                        jsonObject.getBoolean("isExchange"),
                        jsonObject.getInt("deposit"),
                        jsonObject.getString("address"),
                        jsonObject.getInt("rent"),
                        jsonObject.getInt("salePrice"),
                        jsonObject.getString("rentMethod"),
                        jsonObject.getInt("amount"),
                        jsonObject.getString("type"),
                        jsonObject.getString("type1"),
                        jsonObject.getString("type2"),
                        jsonObject.getString("userId"),
                        jsonObject.getDouble("weightPrice")
                    )
                )
            }
            return tmp
        }
    }

    fun getProductListByType(typeName: String, index: Int, amount: Int): ProductsListModel {
        NetworkController.instance.getProductListByType(typeName, index, amount)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {

                            val jsonArray = JSONArray(res)
                            this.items = getProductList(jsonArray)
                            notify(this)
                        }
                    }
                }
            }
            .exec()
        return this
    }

    fun getProductList(
        typeName: String,
        typeName2: String,
        typeName3: String,
        index: Int,
        amount: Int
    ): ProductsListModel {
        NetworkController.instance.getProductList(typeName, typeName2, typeName3, index, amount)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonArray = JSONArray(res)
                            this.items = getProductList(jsonArray)
                            notify(this)
                        }
                    }
                }
            }
            .exec()
        return this
    }
    fun getProductListByKeyWork(
        typeName: String,
        typeName2: String,
        keyWork: String,
        index: Int,
        amount: Int
    ): ProductsListModel {
        NetworkController.instance.getProductListByKeyWork(
            typeName,
            typeName2,
            keyWork,
            index,
            amount
        )
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonArray = JSONArray(res)
                            this.items = getProductList(jsonArray)
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

    fun getProductListBySellerId(id: String, index: Int, amount: Int): ProductsListModel {
        NetworkController.instance.getProductListBySellerId(id, index, amount)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonArray = JSONArray(res)
                            this.items = getProductList(jsonArray)
                            notify(this)
                        }
                    }
                }
            }
            .exec()
        return this
    }

    fun getProductListOnShelf(token: String): ProductsListModel {
        NetworkController.instance.getProductOnShelf(token)
            .onResponse { code, res ->
                when (code) {
                    200 -> {
                        this.items = getProductList(JSONArray(res))
                        Handler(Looper.getMainLooper()).post {
                            notify(this)
                        }
                    }
                }
            }
            .exec()
        return this
    }

    fun getProductListNotOnShelf(token: String): ProductsListModel {
        NetworkController.instance.getProductNotOnShelf(token)
            .onResponse { code, res ->
                when (code) {
                    200 -> {
                        this.items = getProductList(JSONArray(res))
                        Handler(Looper.getMainLooper()).post {
                            notify(this)
                        }
                    }
                }
            }
            .exec()
        return this
    }

    fun getProductListByCart(token: String): ProductsListModel {
        NetworkController.instance.getCartList(token)
            .onResponse { code, res ->
                when (code) {
                    200 -> {
                        this.items = getProductList(JSONArray(res))
                        Handler(Looper.getMainLooper()).post {
                            notify(this)
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
        items.forEach {
            it.closeObserved()
        }
        items.clear()
    }
}