package com.example.RentHelper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.RentHelper.Fragment.*
import com.example.RentHelper.R
import com.example.RentHelper.Tool.ObserverSomethings
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.OrderModel

class OrderActivity : AppCompatActivity() {
    val orderObserver = object : ObserverSomethings<OrderModel>() {
        override fun update(data: OrderModel) {
            val orderId = data.orderId
            val userId = data.product?.userId
            supportFragmentManager.beginTransaction().replace(
                R.id.llProduct,
                OrderProductFragment.newInstance(orderId)
            ).commit()
            supportFragmentManager.beginTransaction()
                .replace(R.id.llNote, OrderNoteFragment.newInstance(orderId)).commit()
            userId?.let {
                supportFragmentManager.beginTransaction().replace(
                    R.id.llUser,
                    OrderUserFragment.newInstance(it)
                ).commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        AccountModel.INSTANCE.token?.let { token ->
            intent.extras?.getString("orderId")?.let { orderId ->
                orderObserver.onBind(OrderModel().getById(token, orderId))
            }
        }
    }
}