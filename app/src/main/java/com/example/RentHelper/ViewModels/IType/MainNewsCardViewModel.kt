package com.example.RentHelper.ViewModels.IType

import android.view.LayoutInflater
import androidx.core.net.toUri
import com.example.RentHelper.Tool.ViewType
import com.example.RentHelper.Tool.CommonAdapter
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.R
import com.example.RentHelper.ViewHolder.NewsProductViewHolder
import com.example.RentHelper.models.ProductsListModel

class MainNewsCardViewModel(val title: String) : Observed<MainNewsCardViewModel>(), IType,
    Observer<ProductsListModel> {
    var seeMore: (() -> Unit)? = null
    val adapter = CommonAdapter.Builder(ArrayList())
        .addType(
            { viewGroup ->
                NewsProductViewHolder(
                    LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.item_list_news_product, viewGroup, false)
                )
            }, ViewType.ProductCardViewModel
        )
        .build()

    fun setSeeMore(action: (() -> Unit)): MainNewsCardViewModel {
        this.seeMore = action
        return this
    }

    fun getProductListByType(index: Int, amount: Int): MainNewsCardViewModel {
        onBind(ProductsListModel().getProductListByType(title, index, amount))
        return this
    }

    fun getProductListBySellerId(sellerId: String, index: Int, amount: Int): MainNewsCardViewModel {
        onBind(ProductsListModel().getProductListBySellerId(sellerId, index, amount))
        return this
    }

    override fun getType(): String {
        return ViewType.MainNewsCardViewModel
    }

    override fun onBind(observed: Observed<ProductsListModel>) {
        observed.register(this)
    }

    var onUpdate: ((productId: String) -> Boolean)? = null
    override fun update(data: ProductsListModel) {
        data.items.forEach {
            if (onUpdate != null) {
                if (!onUpdate?.invoke(it.id)!!) {
                    if (it.pics.size == 0) {
                        adapter.add(
                            ProductCardViewModel(
                                it.id,
                                it.title,
                                it.price,
                                it.getMySaleType(),
                                null
                            )
                        )
                    } else {
                        adapter.add(
                            ProductCardViewModel(
                                it.id, it.title, it.price,
                                it.getMySaleType(),
                                it.pics[0].toUri()
                            )
                        )
                    }
                }
            } else {
                if (it.pics.size == 0) {
                    adapter.add(
                        ProductCardViewModel(
                            it.id,
                            it.title,
                            it.price,
                            it.getMySaleType(),
                            null
                        )
                    )
                } else {
                    adapter.add(
                        ProductCardViewModel(
                            it.id, it.title, it.price,
                            it.getMySaleType(),
                            it.pics[0].toUri()
                        )
                    )
                }

            }


        }
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
        adapter.clear()
    }
}