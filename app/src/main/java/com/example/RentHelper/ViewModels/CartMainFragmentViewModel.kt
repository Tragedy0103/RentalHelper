package com.example.RentHelper.ViewModels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.example.RentHelper.Activity.ProductActivity
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewModels.IType.CartItemViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.ProductModel
import com.example.RentHelper.models.ProductsListModel
import com.facebook.drawee.view.SimpleDraweeView

class CartMainFragmentViewModel {

    val adapter = CommonAdapter.Builder(ArrayList()).addType({
        object : BaseViewHolder<CartItemViewModel>(
            LayoutInflater.from(it.context).inflate(
                R.layout.item_list_cart_product,
                it,
                false
            )
        ) {
            override fun bind(item: CartItemViewModel) {
                itemView.setOnClickListener {
                    Router.router(
                        Router.NextTmp(itemView.context, ProductActivity::class.java,
                            Bundle().apply { putString("ProductId", item.id) })
                    )
                }
                itemView.findViewById<SimpleDraweeView>(R.id.imgProduct)
                    .setImageURI(item.uri, itemView.context)
                itemView.findViewById<TextView>(R.id.tvTitle).text = item.title
                val llType = itemView.findViewById<LinearLayout>(R.id.llType)
                llType.removeAllViews()
                item.mytype.forEach {str->
                    val view = LayoutInflater.from(itemView.context).inflate(R.layout.item_tab_text,llType,false)
                    view.findViewById<TextView>(R.id.tvTab).text = str
                    llType.addView(view)
                }
                itemView.findViewById<TextView>(R.id.tvAmount).text = item.amount.toString()
                val llTradeMethod = itemView.findViewById<LinearLayout>(R.id.llTradeMethod)
                llTradeMethod.removeAllViews()
                item.tradeMethod.forEach { str ->
                    val view = LayoutInflater.from(itemView.context)
                        .inflate(R.layout.item_tab_text, llTradeMethod, false)
                    view.findViewById<TextView>(R.id.tvTab).text = str
                    llTradeMethod.addView(view)
                }
                itemView.findViewById<ImageView>(R.id.imgSet).setOnClickListener { v ->
                    val popup = PopupMenu(itemView.context, v)
                    popup.menuInflater.inflate(R.menu.main_menu, popup.menu)
                    popup.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.itDelete -> {
                                AccountModel.INSTANCE.token?.let { token ->
                                    ProductModel.deleteCart(token, item.id,
                                        { str ->
                                            Toast.makeText(
                                                itemView.context,
                                                str,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            remove(item)
                                        }, { str ->
                                            Toast.makeText(
                                                itemView.context,
                                                str,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        })
                                }
                            }
                        }
                        false
                    }
                    popup.show()

                }
            }
        }
    }, ViewType.CartItemViewModel)
        .build()
    val CartList = object : ObserverSomethings<ProductsListModel>() {
        override fun update(data: ProductsListModel) {
            data.items.forEach {
                adapter.add(
                    CartItemViewModel(
                        it.getFirstImg(),
                        it.id,
                        it.title,
                        it.getMySaleType(),
                        it.amount,
                        it.getMyType()
                    )
                )
            }
        }
    }

    init {
        upDataList()
    }

    fun upDataList() {
        CartList.close()
        adapter.clear()
        AccountModel.INSTANCE.token?.let {
            CartList.onBind(ProductsListModel().getProductListByCart(it))
        }
    }

    fun remove(item: IType) {
        adapter.remove(item)
    }
}