package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewModels.IType.TrideItemsViewModel
import com.example.RentHelper.models.*

class AddOrderViewModel(val salerId: String, val productId: String) : Observed<AddOrderViewModel>() {

    val product: ObserverSomethings<ProductModel> =
        object : ObserverSomethings<ProductModel>() {
            override fun update(data: ProductModel) {
                val saleModeTmp = ArrayList<String>()
                if (data.isRent) {
                    saleModeTmp.add("租借")
                }
                if (data.isSale) {
                    saleModeTmp.add("購買")
                }
                if (data.isExchange) {
                    saleModeTmp.add("交換")
                }
                saleMode = saleModeTmp
                amount = data.amount
                needAmount = setNeedAmount(amount)
                title = data.title
                price = data.price
                rent = data.rent
                deposit = data.deposit
                weight = data.weightPrice
                notify(this@AddOrderViewModel)
            }
        }
    val saler: ObserverSomethings<UserModel> = object : ObserverSomethings<UserModel>() {
        override fun update(data: UserModel) {
            nickName = data.nickName
            email = data.email
            phone = data.phone
            address = data.address
            exchangeItem = data.wishListModel
            notify(this@AddOrderViewModel)
        }
    }

    init {
        getSalerInfo(salerId)
        getProductInfo(productId)
    }
    var tradeMethod :Int  =0
    var title: String = ""
    var nickName: String = ""
    var email: String = ""
    var phone: String = ""
    var address: String = ""
    var saleMode: ArrayList<String>? = null
    var amount: Int = 0
    var weight: Double = 0.0
    var needAmount: ArrayList<String>? = null
    var needAmountNum :Int = 0
    var exchangeItem: ArrayList<WishItemModel>? = null
    var price: Int = 0
    var rent: Int = 0
    var deposit: Int = 0
    var needWeight: Double = 0.0
    var currentWeight: Double = 0.0
    var action: ((currentWeignt: Double) -> Unit)? = null
    fun setNeedAmount(value: Int): ArrayList<String> {
        val tmp = ArrayList<String>()
        for (i in 1..value) {
            tmp.add("" + i)
        }
        return tmp
    }

    var trideItemsAdapter = CommonAdapter.Builder(ArrayList())
        .addType({
            val view =
                LayoutInflater.from(it.context).inflate(R.layout.item_list_trideitems, it, false)
            object : BaseViewHolder<TrideItemsViewModel>(view) {
                override fun bind(item: TrideItemsViewModel) {
                    val cbIsExchange = itemView.findViewById<CheckBox>(R.id.cbIsExchange)
                    cbIsExchange.text = item.name
                    cbIsExchange.setOnCheckedChangeListener { buttonView, isChecked ->
                        item.isExchange = isChecked
                    }
                    val spAmount = itemView.findViewById<Spinner>(R.id.spAmount)
                    spAmount.adapter = item.adapter
                    spAmount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            item.currentAmount = spAmount.selectedItem.toString().toInt()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                    itemView.findViewById<TextView>(R.id.tvWeight).text = item.weight.toString()

                }
            }
        }, ViewType.TrideItemsViewModel)
        .build()

    val exChangeObserver = object : ObserverSomethings<TrideItemsViewModel>() {
        override fun update(data: TrideItemsViewModel) {
            currentWeight = 0.0
            trideItemsAdapter.map {
                if (it is TrideItemsViewModel) {
                    when (it.isExchange) {
                        true -> {
                            currentWeight += (it.getTotalWeight())
                        }
                    }
                }
            }
            action?.invoke(currentWeight)
        }
    }

    fun setNeedWeight(amount: Int) {
        needWeight = weight * amount
    }

    fun addTrideItem(item: TrideItemsViewModel) {
        trideItemsAdapter.add(item)
        exChangeObserver.onBind(item)
    }

    fun trideItemsClear() {
        trideItemsAdapter.clear()
    }

    fun AddOrder(action:(()->Unit)?) {
        val tmp = ArrayList<TrideItemsViewModel>()
        trideItemsAdapter.map {
            if (it is TrideItemsViewModel) {
                if (it.isExchange) {
                    tmp.add(it)
                }
            }
        }
        if(tradeMethod!=2 || currentWeight>=needWeight){
            AccountModel.INSTANCE.token?.let {
                OrderModel().addOrder(it,productId,tradeMethod,tmp,needAmountNum,action)
            }
        }
    }

    fun getSalerInfo(salerId: String) {
        saler.onBind(UserModel().getInfo(salerId))
    }

    fun getProductInfo(productId: String) {
        product.onBind(ProductModel().getById(productId))
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
        product.close()
        saler.close()
    }

    fun getPriceNum(SaleMode: String, needAmount: Int): String {
        return when (SaleMode) {
            "購買" -> {
                (needAmount * price).toString() + "元"
            }
            "租借" -> {
                (needAmount * rent).toString() + "元/日"
            }
            else -> {
                ""
            }
        }
    }
}