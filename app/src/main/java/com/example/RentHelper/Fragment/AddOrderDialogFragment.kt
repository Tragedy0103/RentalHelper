package com.example.RentHelper.Fragment

import android.app.Dialog
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.ViewModels.AddOrderViewModel
import com.example.RentHelper.ViewModels.IType.TrideItemsViewModel


class AddOrderDialogFragment : DialogFragment(), Observer<AddOrderViewModel> {
    var tvTitle: TextView? = null
    var tvNickName: TextView? = null
    var tvEmail: TextView? = null
    var tvPhone: TextView? = null
    var tvAddress: TextView? = null
    var spSaleMode: Spinner? = null
    var tvOnlyAmmountNum: TextView? = null
    var tvPrice: TextView? = null
    var tvTrideItems: TextView? = null
    var tvPriceNum: TextView? = null
    var rcvTrideItems: RecyclerView? = null
    var spNeedAmmount: Spinner? = null
    var btnCancel: Button? = null
    var btnConfirm: Button? = null
    var clAddOrder: ConstraintLayout? = null
    var addOrderViewModel: AddOrderViewModel? = null

    var tvWeight: TextView? = null
    var tvWeightNum: TextView? = null
    var tvNeedWeight: TextView? = null
    var tvNeedWeightNum: TextView? = null
    var tvCurrentWeight: TextView? = null
    var tvCurrentWeightNum: TextView? = null
    var onSuccessAction:(()->Unit)?=null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_addorder, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvNickName = view.findViewById(R.id.tvNickName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        spSaleMode = view.findViewById(R.id.spSaleMode)
        tvOnlyAmmountNum = view.findViewById(R.id.tvOnlyAmmountNum)
        tvPrice = view.findViewById(R.id.tvPrice)
        tvTrideItems = view.findViewById(R.id.tvTrideItems)
        tvPriceNum = view.findViewById(R.id.tvPriceNum)
        rcvTrideItems = view.findViewById(R.id.rcvTrideItems)
        spNeedAmmount = view.findViewById(R.id.spNeedAmmount)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        clAddOrder = view.findViewById(R.id.clAddOrder)
        tvWeight = view.findViewById(R.id.tvWeight)
        tvWeightNum = view.findViewById(R.id.tvWeightNum)
        tvNeedWeight = view.findViewById(R.id.tvNeedWeight)
        tvNeedWeightNum = view.findViewById(R.id.tvNeedWeightNum)
        tvCurrentWeight = view.findViewById(R.id.tvCurrentWeight)
        tvCurrentWeightNum = view.findViewById(R.id.tvCurrentWeightNum)
        btnCancel?.setOnClickListener {
            dismiss()
        }
        btnConfirm?.setOnClickListener {
            addOrderViewModel?.AddOrder {
                onSuccessAction?.invoke()
                dismiss()
            }
        }
    }

    fun myShow(manager: FragmentManager, tag: String?, salerId: String, productId: String) {
        showNow(manager, tag)
        addOrderViewModel = AddOrderViewModel(salerId, productId)
        addOrderViewModel?.let { onBind(it) }
        spSaleMode?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectChange(
                    spSaleMode?.selectedItem?.toString()!!,
                    spNeedAmmount?.selectedItem.toString().toInt()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spNeedAmmount?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addOrderViewModel?.needAmountNum = spNeedAmmount?.selectedItem.toString().toInt()
                selectChange(
                    spSaleMode?.selectedItem?.toString()!!,
                    spNeedAmmount?.selectedItem.toString().toInt()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    fun selectChange(saleMode: String, amount: Int) {
        when (saleMode) {
            "購買" -> {
                val transition = AutoTransition()
                transition.duration = 100
                TransitionManager.beginDelayedTransition(clAddOrder, transition)
                addOrderViewModel?.tradeMethod = 1
                tvPrice?.visibility = View.VISIBLE
                tvPriceNum?.visibility = View.VISIBLE
                tvWeight?.visibility = View.GONE
                tvWeightNum?.visibility = View.GONE
                tvNeedWeight?.visibility = View.GONE
                tvNeedWeightNum?.visibility = View.GONE
                tvCurrentWeight?.visibility = View.GONE
                tvCurrentWeightNum?.visibility = View.GONE
                tvTrideItems?.visibility = View.GONE
                rcvTrideItems?.visibility = View.GONE
                tvPriceNum?.text = addOrderViewModel?.getPriceNum("購買", amount)
            }
            "租借" -> {
                val transition = AutoTransition()
                transition.duration = 100
                TransitionManager.beginDelayedTransition(clAddOrder, transition)
                addOrderViewModel?.tradeMethod = 0
                tvPrice?.visibility = View.VISIBLE
                tvPriceNum?.visibility = View.VISIBLE
                tvWeight?.visibility = View.GONE
                tvWeightNum?.visibility = View.GONE
                tvNeedWeight?.visibility = View.GONE
                tvNeedWeightNum?.visibility = View.GONE
                tvCurrentWeight?.visibility = View.GONE
                tvCurrentWeightNum?.visibility = View.GONE
                tvTrideItems?.visibility = View.GONE
                rcvTrideItems?.visibility = View.GONE
                tvPriceNum?.text = addOrderViewModel?.getPriceNum("租借", amount)

            }
            "交換" -> {
                val transition = AutoTransition()
                transition.duration = 100
                TransitionManager.beginDelayedTransition(clAddOrder, transition)
                addOrderViewModel?.tradeMethod = 2
                tvPrice?.visibility = View.GONE
                tvPriceNum?.visibility = View.GONE
                tvWeight?.visibility = View.VISIBLE
                tvWeightNum?.visibility = View.VISIBLE
                tvTrideItems?.visibility = View.VISIBLE
                tvNeedWeight?.visibility = View.VISIBLE
                tvNeedWeightNum?.visibility = View.VISIBLE
                tvCurrentWeight?.visibility = View.VISIBLE
                tvCurrentWeightNum?.visibility = View.VISIBLE
                rcvTrideItems?.visibility = View.VISIBLE
                rcvTrideItems?.adapter = addOrderViewModel?.trideItemsAdapter
                addOrderViewModel?.setNeedWeight(amount)
                tvNeedWeightNum?.text = addOrderViewModel?.needWeight?.toString()
                addOrderViewModel?.action = {currentWeignt ->
                    tvCurrentWeightNum?.text = currentWeignt.toString()
                }
                rcvTrideItems?.layoutManager = LinearLayoutManager(this.context)
            }
        }
    }

    override fun onBind(observed: Observed<AddOrderViewModel>) {
        observed.register(this)
    }

    override fun update(data: AddOrderViewModel) {
        tvTitle?.text = data.title
        tvNickName?.text = data.nickName
        tvEmail?.text = data.email
        tvPhone?.text = data.phone
        tvAddress?.text = data.address
        data.needAmount?.let {
            spNeedAmmount?.adapter = ArrayAdapter<String>(
                this.context!!,
                R.layout.item_spinner_dropdown,
                it
            )
        }
        data.saleMode?.let {
            spSaleMode?.adapter = ArrayAdapter<String>(
                this.context!!,
                R.layout.item_spinner_dropdown,
                it
            )
        }
        addOrderViewModel?.trideItemsAdapter?.clear()
        addOrderViewModel?.exchangeItem?.forEach {
            addOrderViewModel?.addTrideItem(
                TrideItemsViewModel(
                    it.id!!,
                    this.context!!,
                    R.layout.item_spinner_dropdown,
                    addOrderViewModel?.setNeedAmount(it.RequestQuantity)!!,
                    it.ExchangeItem,
                    it.WeightPoint!!
                )
            )
        }
        tvOnlyAmmountNum?.text = data.amount.toString()
        tvNickName?.text = data.nickName
        tvWeightNum?.text = data.weight.toString()
        tvNeedWeightNum?.text = data.needWeight.toString()
        tvCurrentWeightNum?.text = data.currentWeight.toString()

    }
}