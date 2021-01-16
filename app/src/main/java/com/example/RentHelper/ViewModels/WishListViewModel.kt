package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.RentHelper.ViewModels.IType.WishListItemViewModel
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.WishItemModel
import com.example.RentHelper.models.WishListModel

class WishListViewModel : Observed<WishListViewModel>() {
    companion object{
        val instance:WishListViewModel by lazy { WishListViewModel() }
    }
    var onDeleteImgClickAction: ((item: IType) -> Unit)? = null
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({ viewGroup ->
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_list_wishlist, viewGroup, false)
            object : BaseViewHolder<WishListItemViewModel>(view) {
                override fun bind(item: WishListItemViewModel) {
                    itemView.findViewById<TextView>(R.id.tvName).text = item.name
                    itemView.findViewById<TextView>(R.id.tvValue).text = item.value.toString()
                    itemView.findViewById<TextView>(R.id.tvAmount).text = item.amount.toString()
                    itemView.findViewById<ImageView>(R.id.imgCancle).setOnClickListener {
                        onDeleteImgClickAction?.invoke(item)
                    }
                }
            }
        }, ViewType.WishListItemViewModel)
        .addFooter { viewGroup ->
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_list_wishlist_footer, viewGroup, false)
            view.findViewById<ImageView>(R.id.imgAddWish).setOnClickListener {
                val etName = view.findViewById<EditText>(R.id.etName)
                val etValue = view.findViewById<EditText>(R.id.etValue)
                val etAmount = view.findViewById<EditText>(R.id.etAmount)
                val name: String =etName.text.toString()
                val value: Double =if(etValue.text.toString()==""){0.0}else{etValue.text.toString().toDouble()}
                val amount: Int =if(etAmount.text.toString() ==""){0}else{etAmount.text.toString().toInt()}
                addItems(name, value, amount) {
                    etName.setText("")
                    etValue.setText("")
                    etAmount.setText("")
                }
            }
            object : BaseViewHolder<IType>(view) {
                override fun bind(item: IType) {
                }
            }
        }
        .build()
    var onAdd: ((str: String) -> Unit)? = null
    val wishListViewModel = object : ObserverSomethings<WishListModel>() {
        override fun update(data: WishListModel) {
            data.items.forEach {
                adapter.add(
                    WishListItemViewModel(
                        it.id,
                        it.ExchangeItem,
                        it.WeightPoint!!,
                        it.RequestQuantity
                    )
                )
            }
        }
    }

    init {
        upData()
    }
    fun upData(){
        AccountModel.INSTANCE.token?.let {
            wishListViewModel.close()
            wishListViewModel.onBind(WishListModel().getAllWishList(it))
        }
    }
    fun addItems(name: String, value: Double, amount: Int, onSuccess: () -> Unit?) {
        val wishItemModel = WishItemModel(null, name, amount, value)
        AccountModel.INSTANCE.token?.let {
            wishItemModel
                .addWishItem(it,
                    { str ->
                        onAdd?.invoke(str)
                        onSuccess.invoke()
                        adapter.add(
                            WishListItemViewModel(
                                wishItemModel.id,
                                wishItemModel.ExchangeItem,
                                wishItemModel.WeightPoint!!,
                                wishItemModel.RequestQuantity
                            )
                        )
                    }, { str ->
                        onAdd?.invoke(str)
                    })
        }
    }

    var onDelete: ((str: String) -> Unit)? = null
    fun deleteItems(item: IType) {
        if (item is WishListItemViewModel) {
            AccountModel.INSTANCE.token?.let {
                WishItemModel.deleteWishItem(it, item.id!!,
                    { str ->
                        onDelete?.invoke(str)
                        adapter.remove(item)
                    },
                    { str ->
                        onDelete?.invoke(str)
                    })
            }
        }
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
        adapter.clear()
        onDeleteImgClickAction = null
        onAdd=null
        wishListViewModel.close()
    }
}