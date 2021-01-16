package com.example.RentHelper.ViewModels.IType

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewHolder.ProductViewHolder
import com.example.RentHelper.models.ProductModel
import com.facebook.drawee.view.SimpleDraweeView

class ProductActivityViewModel(val id: String) : Observed<ProductActivityViewModel>(),
    Observer<ProductModel>, IType {
    private var productModel: ProductModel? = null

    init {
        productModel = ProductModel().getById(id)
        onBind(productModel!!)
    }
    var pics: ArrayList<String>? = null
    var title: String? = null
    var description: String? = null
    var saleType: ArrayList<String>? = null
    var price: String? = null
    var rentMethod: String? = null
    var amount: Int? = null
    var myType: ArrayList<String>? = null
    var userId: String? = null
    var email: String? = null
    var phone: String? = null
    var address: String? = null
    val picAdapter = CommonAdapter.Builder(ArrayList())
        .addType({
            object : BaseViewHolder<ProductImagesViewModel>(
                LayoutInflater.from(it.context).inflate(R.layout.item_list_product_image, it, false)
            ) {
                override fun bind(item: ProductImagesViewModel) {
                    if (itemView is SimpleDraweeView) {
                        itemView.setImageURI(item.uri, itemView.context)
                    }
                }
            }
        }, ViewType.ProductImagesViewModel).build()
    var adapter: CommonAdapter? = CommonAdapter.Builder(ArrayList())
        .addType({ viewGroup ->
            ProductViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.item_list_news, viewGroup, false)
            )
        }, ViewType.MainNewsCardViewModel)
        .build()

    fun getOrderProductList(){
        adapter?.clear()
        productModel?.userId?.let {
            adapter?.add(
                MainNewsCardViewModel("其他商品").apply {
                    onUpdate = {
                        it == id
                    }
                    getProductListBySellerId(it, 0, 50)
                }

            )
        }

    }

    override fun getType(): String {
        return ViewType.ProductActivityViewModel
    }

    override fun onBind(observed: Observed<ProductModel>) {
        observed.register(this)
    }

    override fun update(data: ProductModel) {
        Handler(Looper.getMainLooper()).post {

            this.myType = data.getMyType()

            this.pics = data.pics
            this.title = data.title
            this.description = data.description
            var tmpprice = ""
            if (data.isSale) {
                tmpprice += "\n" +
                        "定價：" + data.price
            }
            if (data.isRent) {
                tmpprice += "\n" +
                        "租金：" + data.rent + "\n" +
                        "保證金：" + data.deposit
            }
            if (data.isExchange) {
                tmpprice += "\n" +
                        "權重：" + data.weightPrice
            }
            this.saleType = data.getMySaleType()
            this.price =tmpprice
            this.rentMethod = data.rentMethod

            this.amount = data.amount
            this.myType =data.getMyType()
            this.userId = data.userId
            picAdapter.clear()
            pics?.forEach {
                picAdapter.add(ProductImagesViewModel(it))
            }

            notify(this)
        }
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
        adapter?.clear()
        adapter = null
        productModel?.closeObserved()
        title = null
        description = null
        saleType = null
        price = null
        rentMethod = null
        amount = null
        myType = null
        userId = null
    }
}