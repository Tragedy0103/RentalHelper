package com.example.RentHelper.ViewModels.IType

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.core.net.toUri
import com.example.RentHelper.Activity.ModifyProductActivity
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.ProductModel
import com.example.RentHelper.models.ProductsListModel
import com.facebook.drawee.view.SimpleDraweeView

class MyStorePageViewModel(val str: String) : IType {
    var itemLongClickAction: ((id: String) -> Unit)? = null
    var itemCancelLongClickAction: (() -> Unit)? = null
    var itemClickAction: (() -> Unit)? = null
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<MyStoreItemViewModel>(
                LayoutInflater.from(it.context).inflate(R.layout.item_list_product, it, false)
            ) {
                override fun bind(item: MyStoreItemViewModel) {
                    itemView.findViewById<SimpleDraweeView>(R.id.imgProduct)
                        .setImageURI(item.uri, itemView.context)
                    itemView.findViewById<TextView>(R.id.tvTitle).text = item.title
                    itemView.findViewById<TextView>(R.id.tvPrice).text = item.price
                    itemView.findViewById<TextView>(R.id.tvAmount).text = item.amount.toString()
                    val llType = itemView.findViewById<LinearLayout>(R.id.llType)
                    llType.removeAllViews()
                    item.saleType.forEach {str->
                        val view = LayoutInflater.from(itemView.context).inflate(R.layout.item_tab_text,llType,false)
                        view.findViewById<TextView>(R.id.tvTab).text = str
                        llType.addView(view)
                    }
                    itemView.findViewById<ImageView>(R.id.imgSet).setOnClickListener { v ->
                        val popup = PopupMenu(itemView.context, v)
                        popup.menuInflater.inflate(R.menu.mystore_item_menu, popup.menu)
                        popup.setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.itEdit -> {
                                    val bundle = Bundle()
                                    bundle.putString("ProductId", item.id)
                                    Router.router(
                                        Router.NextTmp(
                                            itemView.context, ModifyProductActivity::class.java,
                                            bundle
                                        )
                                    )
                                }
                                R.id.itDelete -> {
                                    AccountModel.INSTANCE.token?.let { token ->
                                        ProductModel.delete(token, item.id,
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
                    itemView.setOnClickListener {
                        itemClickAction?.invoke()

                    }
                    itemView.setOnLongClickListener { v ->
                        if (!itemView.isLongClickable) {
                            itemLongClickAction?.invoke(item.id)
                            true
                        } else {
                            itemCancelLongClickAction?.invoke()
                            false
                        }
                    }
                }
            }
        }, ViewType.MyStoreItemViewModel)
        .build()

    var items = object : ObserverSomethings<ProductsListModel>() {
        override fun update(data: ProductsListModel) {
            adapter.clear()
            data.items.forEach {
                if (it.pics.size == 0) {
                    adapter.add(
                        MyStoreItemViewModel(
                            null,
                            it.id,
                            it.title,
                            it.amount,
                            it.getMySaleType(),
                            it.getPrice()
                        )
                    )
                } else {
                    adapter.add(
                        MyStoreItemViewModel(
                            it.pics.get(0).toUri(),
                            it.id,
                            it.title,
                            it.amount,
                            it.getMySaleType(),
                            it.getPrice()
                        )
                    )
                }

            }
        }
    }

    fun remove(item: IType) {
        if (item is MyStoreItemViewModel) {
            adapter.remove(item)
        }
    }
    fun getProductList() {
        when (str) {
            "OnShelf" -> {
                items.close()
                AccountModel.INSTANCE.token?.let {
                    items.onBind(ProductsListModel().getProductListOnShelf(it))
                }
            }
            "OffShelf" -> {
                items.close()
                AccountModel.INSTANCE.token?.let {
                    items.onBind(ProductsListModel().getProductListNotOnShelf(it))
                }
            }
        }
    }

    override fun getType(): String {
        return ViewType.MyStorePageViewModel
    }

}