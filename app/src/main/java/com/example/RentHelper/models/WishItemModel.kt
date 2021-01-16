package com.example.RentHelper.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.RentHelper.Controller.NetworkController
import com.example.RentHelper.Tool.Observed

class WishItemModel(
    var id: String?,
    val ExchangeItem: String,
    val RequestQuantity: Int,
    val WeightPoint: Double?
) :
    Observed<WishItemModel>() {


    companion object {
        fun deleteWishItem(
            token: String,
            id: String,
            onSuccess: ((str: String) -> Unit)?,
            onFail: ((str: String) -> Unit)?
        ) {
            NetworkController.instance.deleteWishItem(token, id)
                .onResponse { code, res ->
                    Log.d("delete",""+code + res)
                    Handler(Looper.getMainLooper()).post {
                        when (code) {
                            200 -> {
                                onSuccess?.invoke("刪除成功")
                            }
                            else -> {
                                onFail?.invoke("刪除失敗")
                            }
                        }
                    }
                }
                .onFailure {

                    Log.d("delete",it.toString())
                }
                .exec()
        }

    }


    fun addWishItem(
        token: String,
        onSuccess: ((str: String) -> Unit)?,
        onFail: ((str: String) -> Unit)?
    ) {
        NetworkController.instance.addWishItem(token, ExchangeItem, RequestQuantity, WeightPoint!!)
            .onResponse { code, res ->
                Handler(Looper.getMainLooper()).post {
                    when (code) {
                        200 -> {
                            onSuccess?.invoke("新增成功")
                            this.id = res
                            notify(this)
                        }

                        else -> {
                            onFail?.invoke("新增失敗")
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