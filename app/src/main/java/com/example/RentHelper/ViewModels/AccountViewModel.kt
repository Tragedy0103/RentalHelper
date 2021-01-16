package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import android.widget.TextView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewModels.IType.AccountListItemViewModel

class AccountViewModel : Observed<AccountViewModel>(){

    var adapter :CommonAdapter = CommonAdapter.Builder(ArrayList())
        .addType({viewGroup ->
            val view  = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_account_main,viewGroup,false)
            object : BaseViewHolder<AccountListItemViewModel>(view) {
                override fun bind(item: AccountListItemViewModel) {
                    view.findViewById<TextView>(R.id.tvString).text  = item.str
                    view.setOnClickListener { item.action?.invoke() }
                }
            }
        },ViewType.AccountListItemViewModel)
        .build()
    fun addItem (accountListItemViewModel: AccountListItemViewModel){
        this.adapter.add(accountListItemViewModel)
    }
    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {

    }
}