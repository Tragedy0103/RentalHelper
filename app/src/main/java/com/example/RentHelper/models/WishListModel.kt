package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import org.json.JSONArray
import org.json.JSONObject

class WishListModel : Observed<WishListModel>() {

    val items = ArrayList<WishItemModel>()

    fun addItem(item: WishItemModel): WishListModel {
        items.add(item)
        notify(this)
        return this
    }

    companion object {
        fun createWishList(jsonArray: JSONArray): ArrayList<WishItemModel> {
            val tmp = ArrayList<WishItemModel>()
            for (i in 0 until jsonArray.length()) {
                tmp.add(createWishItem(jsonArray.getJSONObject(i)))
            }
            return tmp
        }

        fun createWishItem(jsonObject: JSONObject): WishItemModel {
            return WishItemModel(
                jsonObject.getString("id"),
                jsonObject.getString("exchangeItem"),
                jsonObject.getInt("requestQuantity"),
                jsonObject.getDouble("weightPoint")
            )
        }
    }


    fun getAllWishList(token: String): WishListModel {
        NetworkController.instance.getWishList(token)
            .onResponse { code, res ->
                when(code){
                    200->{
                        items.addAll(createWishList(JSONArray(res)))
                        Handler(Looper.getMainLooper()).post {
                            notify(this)
                        }
                    }
                }

            }
            .exec()
        return this
    }

    fun getOnShelfWishList(token: String): WishListModel {
        NetworkController.instance.getWishListOnShelf(token)
            .onResponse { code, res ->
                createWishList(JSONArray(res))
                Handler(Looper.getMainLooper()).post {
                    notify(this)
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