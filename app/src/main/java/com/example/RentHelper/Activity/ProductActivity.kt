package com.example.RentHelper.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.RentHelper.Fragment.AddOrderDialogFragment
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Router
import com.example.RentHelper.ViewModels.IType.ProductActivityViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.ProductModel

class ProductActivity : AppCompatActivity(), Observer<ProductActivityViewModel> {
    private var tvTitle: TextView? = null
    private var tvPrice: TextView? = null
    private var llType: LinearLayout? = null
    private var tvRentMethon: TextView? = null
    private var llSaleType: LinearLayout? = null
    private var tvAmount: TextView? = null
    private var tvDescription: TextView? = null
    private var rcvOtherProduct: RecyclerView? = null
    private var btnAddToCar: Button? = null
    private var btnBuy: Button? = null
    private var vpProductImg: ViewPager2? = null
    var productActivityViewModel: ProductActivityViewModel? = null
    var addOrderDialogFragment: AddOrderDialogFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val productId = intent.extras?.getString("ProductId")
        productId?.run {
            productActivityViewModel = ProductActivityViewModel(productId)
            onBind(productActivityViewModel!!)
        }
        tvTitle = findViewById(R.id.tvTitle)
        tvPrice = findViewById(R.id.tvPrice)
        llType = findViewById(R.id.llType)
        tvRentMethon = findViewById(R.id.tvRentMethon)
        llSaleType = findViewById(R.id.llSaleType)
        tvAmount = findViewById(R.id.tvAmount)
        tvDescription = findViewById(R.id.tvDescription)
        rcvOtherProduct = findViewById(R.id.rcvOtherProduct)
        btnAddToCar = findViewById(R.id.btnAddToCar)
        btnBuy = findViewById(R.id.btnBuy)
        vpProductImg = findViewById(R.id.vpProductImg)
        addOrderDialogFragment = AddOrderDialogFragment()
        addOrderDialogFragment?.onSuccessAction = {finish()}
        vpProductImg?.adapter = productActivityViewModel?.picAdapter
        rcvOtherProduct?.adapter = productActivityViewModel?.adapter
        rcvOtherProduct?.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        btnAddToCar?.setOnClickListener {
            AccountModel.INSTANCE.token?.let { token ->
                productActivityViewModel?.let { viewModel ->
                    ProductModel.addCart(token, viewModel.id, { res ->
                        Toast.makeText(baseContext, res, Toast.LENGTH_SHORT).show()
                        finish()
                    },
                        { res ->
                            Toast.makeText(baseContext, res, Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }
        btnBuy?.setOnClickListener {
            val view =
                LayoutInflater.from(this)
                    .inflate(R.layout.item_dialog_login, null, false)
            val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
            view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
                Router.changeActivity(this, LoginActivity::class.java, null)
                dialog.hide()
            }
            view.findViewById<Button>(R.id.btnNo)?.setOnClickListener {
                dialog.hide()
            }
            when (AccountModel.INSTANCE.stage) {
                AccountModel.Stage.LOGIN -> {
                    addOrderDialogFragment?.myShow(
                        supportFragmentManager,
                        "order",
                        productActivityViewModel?.userId!!,
                        productActivityViewModel?.id!!
                    )
                }
                AccountModel.Stage.UNLOGIN -> {
                    dialog.show()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        productActivityViewModel?.getOrderProductList()
    }
    override fun onPause() {
        super.onPause()
        productActivityViewModel?.adapter?.clear()
    }

    override fun onBind(observed: Observed<ProductActivityViewModel>) {
        observed.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        productActivityViewModel?.unregister(this)
        productActivityViewModel?.closeObserved()
        productActivityViewModel = null
    }

    override fun update(data: ProductActivityViewModel) {
        llType?.let {
            it.removeAllViews()
            data.myType?.forEach { str->
                val view = LayoutInflater.from(this).inflate(R.layout.item_tab_text,it,false)
                view.findViewById<TextView>(R.id.tvTab).text = str
                it.addView(view)
            }
        }
        tvAmount?.text = data.amount?.toString()
        tvDescription?.text = data.description
        tvPrice?.text = data.price
        tvRentMethon?.text = data.rentMethod
        tvTitle?.text = data.title
        llSaleType?.let {
            it.removeAllViews()
            data.saleType?.forEach { str->
                val view = LayoutInflater.from(this).inflate(R.layout.item_tab_text,it,false)
                view.findViewById<TextView>(R.id.tvTab).text = str
                it.addView(view)
            }
        }
        productActivityViewModel?.getOrderProductList()
    }
}