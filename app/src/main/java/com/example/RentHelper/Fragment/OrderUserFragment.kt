package com.example.RentHelper.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.RentHelper.R
import com.example.RentHelper.Tool.ObserverSomethings
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.UserModel

class OrderUserFragment : Fragment(R.layout.fragment_order_user) {
    companion object {
        fun newInstance(orderId: String): OrderUserFragment {
            val fragment = OrderUserFragment()
            val arg = Bundle()
            arg.putString("userId", orderId)
            fragment.arguments = arg
            return fragment
        }
    }

    var tvNickName: TextView? = null
    var tvEmail: TextView? = null
    var tvPhone: TextView? = null
    var tvAddress: TextView? = null
    var tvSalerInfo: TextView? = null
    private var userId: String? = null
    private val userObserved = object : ObserverSomethings<UserModel>() {
        override fun update(data: UserModel) {
            tvNickName?.text = if (data.nickName == "") {
                data.name
            } else {
                data.nickName
            }
            tvEmail?.text = data.email
            tvPhone?.text = data.phone
            tvAddress?.text = data.address
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString("userId")
        userId?.let { userObserved.onBind(UserModel().getInfo(it)) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNickName = view.findViewById(R.id.tvNickName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvSalerInfo = view.findViewById(R.id.tvSalerInfo)
        tvSalerInfo?.text = if (userId != AccountModel.INSTANCE.id) {
            "賣家資訊"
        } else {
            "買家資訊"
        }
    }
}