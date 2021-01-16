package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed
import org.json.JSONArray

class OrderListModel : Observed<OrderListModel>() {
    val orders = ArrayList<OrderModel>()

    companion object {
        fun getOrderList(jsonArray: JSONArray): ArrayList<OrderModel> {
            val tmp = ArrayList<OrderModel>()
            for (i in 0 until jsonArray.length()) {
                tmp.add(
                    OrderModel().getOrder(jsonArray.getJSONObject(i))
                )
            }
            return tmp
        }
    }

    fun getAllOrders(token: String): OrderListModel {
        NetworkController.instance.getOwnOrder(token)
            .onResponse { code, res ->
                val jsonArray = JSONArray(res)
                orders.addAll(getOrderList(jsonArray))
                Handler(Looper.getMainLooper()).post {
                    notify(this)
                }
            }
            .exec()
        return this
    }

    fun getSellOrders(token: String, status: String): OrderListModel {
        NetworkController.instance.getSellOrder(token, status)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonArray = JSONArray(res)
                            orders.clear()
                            orders.addAll(getOrderList(jsonArray))
                            notify(this)
                        }
                    }
                }
            }
            .exec()
        return this
    }

    fun getBuyOrders(token: String, status: String): OrderListModel {
        NetworkController.instance.getBuyOrder(token, status)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            val jsonArray = JSONArray(res)
                            orders.clear()
                            orders.addAll(getOrderList(jsonArray))
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

    }
}