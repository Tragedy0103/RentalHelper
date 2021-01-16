package com.example.RentHelper.Fragment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.Tool.Router
import com.example.RentHelper.ViewModels.AccountInfoViewModel
import com.example.RentHelper.models.AccountModel

class AccountInfoFragment : Fragment(), Observer<AccountInfoViewModel> {
    var clAccountInfo: ConstraintLayout? = null
    var tvEmail: TextView? = null
    var etEmail: EditText? = null
    var tvName: TextView? = null
    var etName: EditText? = null
    var tvNickName: TextView? = null
    var etNickName: EditText? = null
    var tvPhone: TextView? = null
    var etPhone: EditText? = null
    var tvAddress: TextView? = null
    var etAddress: EditText? = null
    var cvLogout:Button?=null
    var btnModify: Button? = null
    var btnCancel: Button? = null
    var btnConfirm: Button? = null
    var modifySet: ConstraintSet? = null
    var normalSet: ConstraintSet? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    private fun changeConstraintLayout(
        constraintSet: ConstraintSet,
        time: Long,
        targetCl: ConstraintLayout
    ) {
        val transition = AutoTransition()
        transition.duration = time
        TransitionManager.beginDelayedTransition(targetCl, transition)
        constraintSet.applyTo(targetCl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clAccountInfo = view.findViewById(R.id.clAccountInfo)
        tvEmail = view.findViewById(R.id.tvEmail)
        etEmail = view.findViewById(R.id.etEmail)
        tvName = view.findViewById(R.id.tvName)
        etName = view.findViewById(R.id.etName)
        tvNickName = view.findViewById(R.id.tvNickName)
        etNickName = view.findViewById(R.id.etNickName)
        tvPhone = view.findViewById(R.id.tvPhone)
        etPhone = view.findViewById(R.id.etPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        etAddress = view.findViewById(R.id.etAddress)
        btnModify = view.findViewById(R.id.btnModify)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        cvLogout = view.findViewById(R.id.cvLogout)
        cvLogout?.setOnClickListener {
            context?.let { context ->
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_dialog_login, null, false)
                val dialog = AlertDialog.Builder(context)
                    .setView(view)
                    .create()
                view.findViewById<TextView>(R.id.tvtitle).text = "是否登出?"
                view.findViewById<TextView>(R.id.tvIsLogin).text = ""
                view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
                    it.context.getSharedPreferences(
                        "User",
                        AppCompatActivity.MODE_PRIVATE
                    ).edit().clear().apply()
                    AccountModel.INSTANCE.logout()
                    Router.router(
                        Router.NextTmp(
                            it.context,
                            MainFragment(), R.id.llMain, fragmentManager!!, "not",false
                        )
                    )
                    dialog.hide()
                }
                view.findViewById<Button>(R.id.btnNo)?.setOnClickListener {
                    dialog.hide()
                }
                dialog.show()
            }
        }
        onBind(AccountInfoViewModel.instance)
        normalSet = ConstraintSet().also { it.clone(clAccountInfo) }
        modifySet = ConstraintSet().also { it.clone(this.context,R.layout.detail_account_info) }
        btnModify?.setOnClickListener {
            toModify()
//            (this.context as MainActivity).back
        }
        btnConfirm?.setOnClickListener {
            AccountInfoViewModel.instance.InfoModify(etName?.text?.toString()!!,etPhone?.text?.toString()!!,etNickName?.text?.toString()!!,etAddress?.text?.toString()!!)
        }
        btnCancel?.setOnClickListener {
            toNormal()
        }
        AccountInfoViewModel.instance.onModifySuccess={
            toNormal()
        }
    }
    fun toModify(){
        changeConstraintLayout(modifySet!!, 700, clAccountInfo!!)
        tvAddress?.visibility=View.GONE
        tvEmail?.visibility=View.GONE
        tvName?.visibility=View.GONE
        tvNickName?.visibility=View.GONE
        tvPhone?.visibility=View.GONE
        etAddress?.setText(tvAddress?.text)
        etEmail?.setText(tvEmail?.text)
        etName?.setText(tvName?.text)
        etNickName?.setText(tvNickName?.text)
        etPhone?.setText(tvPhone?.text)
        etAddress?.visibility=View.VISIBLE
        etEmail?.visibility=View.VISIBLE
        etName?.visibility=View.VISIBLE
        etNickName?.visibility=View.VISIBLE
        etPhone?.visibility=View.VISIBLE
    }

    fun toNormal(){
        changeConstraintLayout(normalSet!!, 700, clAccountInfo!!)
        tvAddress?.visibility=View.VISIBLE
        tvEmail?.visibility=View.VISIBLE
        tvName?.visibility=View.VISIBLE
        tvNickName?.visibility=View.VISIBLE
        tvPhone?.visibility=View.VISIBLE
        etAddress?.visibility=View.GONE
        etEmail?.visibility=View.GONE
        etName?.visibility=View.GONE
        etNickName?.visibility=View.GONE
        etPhone?.visibility=View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        AccountInfoViewModel.instance.closeObserved()
    }

    override fun onBind(observed: Observed<AccountInfoViewModel>) {
        observed.register(this)
    }
    override fun update(data: AccountInfoViewModel) {
        tvAddress?.text = data.address
        tvEmail?.text = data.email
        tvName?.text = data.name
        tvNickName?.text = data.nickName
        tvPhone?.text = data.phone
    }
}