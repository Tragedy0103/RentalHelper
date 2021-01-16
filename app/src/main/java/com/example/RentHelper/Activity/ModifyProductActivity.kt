package com.example.RentHelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.Tool.ObserverSomethings
import com.example.RentHelper.ViewModels.IType.ImgViewModel
import com.example.RentHelper.ViewModels.ModifyProductActivityViewModel
import com.example.RentHelper.ViewModels.ModifyProductActivityViewModel.*
import java.io.FileNotFoundException


class ModifyProductActivity : AppCompatActivity(), Observer<ModifyProductActivityViewModel> {
    var rcvImg: RecyclerView? = null
    var etTitle: EditText? = null
    var etDescription: EditText? = null
    var clAddProduct: ConstraintLayout? = null
    var clMoney: ConstraintLayout? = null
    var clChange: ConstraintLayout? = null
    var etPrice: EditText? = null
    var etRent: EditText? = null
    var etDeposit: EditText? = null
    var etRentMethod: EditText? = null
    var etAmount: EditText? = null
    var etWeight:EditText?=null
    var etAddress:EditText?=null
    var cbSale: CheckBox? = null
    var cbRent: CheckBox? = null
    var cbChange: CheckBox? = null
    var btnCancel: Button? = null
    var btnAdd: Button? = null
    var spType1: Spinner? = null
    var spType2: Spinner? = null
    var spType3: Spinner? = null
    var modifyProductActivityViewModel: ModifyProductActivityViewModel? =null
    private var nochangeSet: ConstraintSet? = null
    private var changeSet: ConstraintSet? = null
    private var saleSet: ConstraintSet? = null
    private var rentSet: ConstraintSet? = null
    private var nothingSet: ConstraintSet? = null
    private var saleRentSet: ConstraintSet? = null
    val transition = AutoTransition()
    val changeObserver= object :ObserverSomethings<ChangeObserved>(){
        override fun update(data: ChangeObserved) {
            saleTypeRouter(data.isRent, data.isSale, data.isExchange)
        }
    }

    private fun changeSet(constraintLayout: ConstraintLayout, constraintSet: ConstraintSet) {
        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        constraintSet.applyTo(constraintLayout)
    }

    private fun saleTypeRouter(isRent: Boolean, isSale: Boolean, isChange: Boolean) {
        when {
            isRent && isSale -> {
                changeSet(clMoney!!, saleRentSet!!)
            }
            !isRent && isSale -> {
                changeSet(clMoney!!, saleSet!!)
            }
            isRent && !isSale -> {
                changeSet(clMoney!!, rentSet!!)
            }
            !isRent && !isSale -> {
                changeSet(clMoney!!, nothingSet!!)
            }
        }
        when (isChange) {
            true -> {
                changeSet(clChange!!, changeSet!!)
            }
            false -> {
                changeSet(clChange!!, nochangeSet!!)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        intent?.extras?.getString("ProductId")?.let {
            modifyProductActivityViewModel=ModifyProductActivityViewModel(it)
        }
        rcvImg = findViewById(R.id.rcvImg)
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        clAddProduct = findViewById(R.id.clAddProduct)
        clMoney = findViewById(R.id.clMoney)
        clChange = findViewById(R.id.clChange)
        etPrice = findViewById(R.id.etPrice)
        etRent = findViewById(R.id.etRent)
        etDeposit = findViewById(R.id.etDeposit)
        etRentMethod = findViewById(R.id.etRentMethod)
        etAmount = findViewById(R.id.etAmount)
        etWeight = findViewById(R.id.etWeight)
        etAddress = findViewById(R.id.etAddress)
        cbSale = findViewById(R.id.cbSale)
        cbRent = findViewById(R.id.cbRent)
        cbChange = findViewById(R.id.cbChange)
        btnCancel = findViewById(R.id.btnCancel)
        btnAdd = findViewById(R.id.btnAdd)
        spType1 = findViewById(R.id.spType1)
        spType2 = findViewById(R.id.spType2)
        spType3 = findViewById(R.id.spType3)
        rcvImg?.adapter = modifyProductActivityViewModel?.ImgAdapter
        rcvImg?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        onBind(modifyProductActivityViewModel!!)
        changeObserver.onBind(modifyProductActivityViewModel?.changeObserved!!)
        nochangeSet = ConstraintSet().also { it.clone(clChange) }
        changeSet = ConstraintSet().also { it.clone(this, R.layout.detail_addproduct_change) }
        saleSet = ConstraintSet().also { it.clone(this, R.layout.detail_addproduct_saletype_sale) }
        rentSet = ConstraintSet().also { it.clone(this, R.layout.detail_addproduct_saletype_rent) }
        nothingSet = ConstraintSet().also { it.clone(clMoney) }
        saleRentSet =
            ConstraintSet().also { it.clone(this, R.layout.detail_addproduct_saletype_sale_rent) }
        transition.duration = 700
        cbSale?.setOnCheckedChangeListener { _, isChecked ->
            modifyProductActivityViewModel?.changeObserved?.isSale = isChecked
            modifyProductActivityViewModel?.isSale = isChecked
        }
        cbRent?.setOnCheckedChangeListener { _, isChecked ->
            modifyProductActivityViewModel?.changeObserved?.isRent = isChecked
            modifyProductActivityViewModel?.isRent = isChecked
        }
        cbChange?.setOnCheckedChangeListener { _, isChecked ->
            modifyProductActivityViewModel?.changeObserved?.isExchange = isChecked
            modifyProductActivityViewModel?.isExchange = isChecked
        }
        modifyProductActivityViewModel?.imgFooterAction = {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }

        spType1?.adapter =ArrayAdapter<String>(
            this,
            R.layout.item_spinner_dropdown,
            (modifyProductActivityViewModel?.createType1()!!)
        )

        spType1?.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spType2?.adapter = ArrayAdapter<String>(
                    this@ModifyProductActivity,
                    R.layout.item_spinner_dropdown,
                    (modifyProductActivityViewModel?.createType2(spType1?.selectedItem.toString())!!)
                )
                spType3?.adapter = ArrayAdapter<String>(
                    this@ModifyProductActivity,
                    R.layout.item_spinner_dropdown,
                    (modifyProductActivityViewModel?.createType3(spType1?.selectedItem.toString())!!)
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        btnAdd?.text = "確認修改"
        btnAdd?.setOnClickListener {
            modifyProductActivityViewModel?.modifyProduct(etTitle?.text?.toString()!!,
            etDescription?.text?.toString()!!,
            cbSale?.isChecked!!,
            cbRent?.isChecked!!,
            cbChange?.isChecked!!,
                getInt(etDeposit?.text?.toString()),
                etAddress?.text?.toString()!!,
                getInt(etRent?.text?.toString()),
                getInt(etPrice?.text?.toString()),
                getDouble(etWeight?.text?.toString()),
                etRentMethod?.text?.toString()!!,
                getInt(etAmount?.text?.toString()),
                spType1?.selectedItem?.toString()!!,
                spType2?.selectedItem?.toString()!!,
                spType3?.selectedItem?.toString()!!,
                {  str->
                    showToast(str)
                    finish()
                },
                {   str->
                    showToast(str)
                }
            )
        }
    }
    fun showToast(str: String){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }

    fun getInt(str:String?):Int{
        return when (str ){
            null,"null","" -> 0
            else -> str.toInt()
        }
    }
    fun getDouble(str:String?): Double {
        return when (str ){
            null,"null","" -> 0.0
            else -> str.toDouble()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val uri: Uri = data?.data!!
            val cr = this.contentResolver
            try {
                modifyProductActivityViewModel?.addImg(ImgViewModel(cr, uri))
            } catch (e: FileNotFoundException) {
                Log.e("Exception", e.message, e)
            }
        } else {
            Log.i("MainActivtiy", "operation error")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        modifyProductActivityViewModel?.closeObserved()
        modifyProductActivityViewModel = null
          nochangeSet = null
          changeSet = null
          saleSet = null
          rentSet = null
          nothingSet = null
          saleRentSet = null
    }
    override fun onBind(observed: Observed<ModifyProductActivityViewModel>) {
        observed.register(this)
    }

    override fun update(data: ModifyProductActivityViewModel) {
        etAddress?.setText(data.address)
        etAmount?.setText(data.amount?.toString())
        etDeposit?.setText(data.deposit?.toString())
        etDescription?.setText(data.description)
        etPrice?.setText(data.salePrice?.toString())
        etRent?.setText(data.rent?.toString())
        etRentMethod?.setText(data.rentMethod)
        etTitle?.setText(data.title)
        etWeight?.setText(data.weightPrice?.toString())
        cbChange?.isChecked = data.isExchange
        cbRent?.isChecked = data.isRent
        cbSale?.isChecked = data.isSale
        spType1?.setSelection(data.getType1Position())
        spType2?.setSelection(data.getType2Position())
        spType3?.setSelection(data.getType3Position())
    }


}