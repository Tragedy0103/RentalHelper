package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.RentHelper.R
import com.example.RentHelper.Tool.ObserverSomethings
import com.example.RentHelper.ViewModels.OrderProductFragmentViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.OrderModel

class OrderProductFragment : Fragment() {

    companion object {
        fun newInstance(orderId: String): OrderProductFragment {
            val fragment = OrderProductFragment()
            val arg = Bundle()
            arg.putString("orderId", orderId)
            fragment.arguments = arg
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_order_product, container, false)

    var orderId: String? = null
    var vpProductImg: ViewPager2? = null
    var tvTitle: TextView? = null
    var llType: LinearLayout? = null
    var tvRentMethon: TextView? = null
    var tvAmount: TextView? = null
    var tvValue: TextView? = null
    var btnNextState: Button? = null
    var tvState: TextView? = null
    var tvStateDate: TextView? = null
    var tvStateTime: TextView? = null
    var tvDescription: TextView? = null
    var orderProductFragmentViewModel: OrderProductFragmentViewModel? = null
    val orderObserver = object : ObserverSomethings<OrderModel>() {
        override fun update(data: OrderModel) {
            tvTitle?.text = data.product?.title
            llType?.let {
                it.removeAllViews()
                data.product?.getMyType()?.forEach { str->
                    val view = LayoutInflater.from(this@OrderProductFragment.context).inflate(R.layout.item_tab_text,it,false)
                    view.findViewById<TextView>(R.id.tvTab).text = str
                    it.addView(view)
                }
            }
            tvRentMethon?.text = data.getMyTradeMethod()
            tvAmount?.text = data.tradeQuantity.toString()
            tvValue?.text = data.getMyValue()
            tvState?.text = data.status
            if (data.getMyCurrentStateDate().isNotEmpty()) {
                tvStateDate?.text = data.getMyCurrentStateDate()[0]
                tvStateTime?.text = data.getMyCurrentStateDate()[1]
            }
            tvDescription?.text = data.product?.description
            if (orderProductFragmentViewModel?.order?.product?.userId == AccountModel.INSTANCE.id) {
                when (orderProductFragmentViewModel?.order?.status) {
                    "已立單", "歸還已寄出" -> {
                        btnNextState?.visibility = View.VISIBLE
                    }
                    else -> {
                        btnNextState?.visibility = View.GONE
                    }
                }
            } else {
                when (orderProductFragmentViewModel?.order?.status) {
                    "已立單", "歸還已寄出", "已結單" -> {
                        btnNextState?.visibility = View.GONE
                    }
                    else -> {
                        btnNextState?.visibility = View.VISIBLE
                    }
                }
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderId = arguments?.getString("orderId")
        orderProductFragmentViewModel = orderId?.let {
            val tmp = OrderProductFragmentViewModel(it)
            orderObserver.onBind(tmp.order)
            tmp
        }
        vpProductImg = view.findViewById(R.id.vpProductImg)
        tvTitle = view.findViewById(R.id.tvTitle)
        llType = view.findViewById(R.id.llType)
        tvRentMethon = view.findViewById(R.id.tvRentMethon)
        tvAmount = view.findViewById(R.id.tvAmount)
        tvValue = view.findViewById(R.id.tvValue)


        btnNextState = view.findViewById(R.id.btnNextState)
        AccountModel.INSTANCE.token?.let { token ->
            btnNextState?.setOnClickListener {
                context?.let { context ->
                    val view =LayoutInflater.from(context)
                            .inflate(R.layout.item_dialog_login, null, false)
                    val dialog = AlertDialog.Builder(context)
                        .setView(view)
                        .create()
                    view.findViewById<TextView>(R.id.tvtitle).text = "是否切換狀態?"
                    view.findViewById<TextView>(R.id.tvIsLogin).text = ""
                    view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
                        orderProductFragmentViewModel?.order?.ChangeStatus(token, { str ->
                            Toast.makeText(this.context, str, Toast.LENGTH_SHORT).show()
                            orderProductFragmentViewModel?.order?.run {
                                this.getById(token, this.orderId)
                            }
                        }, { str ->
                            Toast.makeText(this.context, str, Toast.LENGTH_SHORT).show()
                        })
                        dialog.hide()
                    }
                    view.findViewById<Button>(R.id.btnNo)?.setOnClickListener {
                        dialog.hide()
                    }
                    dialog.show()
                }


            }
        }

        tvState = view.findViewById(R.id.tvState)
        tvStateDate = view.findViewById(R.id.tvStateDate)
        tvStateTime = view.findViewById(R.id.tvStateTime)
        tvDescription = view.findViewById(R.id.tvDescription)
        vpProductImg?.adapter = orderProductFragmentViewModel?.adapter
    }
}