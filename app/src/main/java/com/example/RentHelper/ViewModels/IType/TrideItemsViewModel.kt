package com.example.RentHelper.ViewModels.IType

import android.content.Context
import android.widget.ArrayAdapter
import com.example.RentHelper.Tool.IType
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.ViewType

class TrideItemsViewModel(
    val wishItemId:String,
    context: Context,
    resource: Int,
    items: ArrayList<String>,
    val name: String,
    val weight: Double
) : Observed<TrideItemsViewModel>(), IType {
    var adapter: ArrayAdapter<String> = ArrayAdapter(context, resource, items)
    var currentAmount :Int = 0
        set(value) {
            field = value
            if(isExchange){
                notify(this)
            }
        }
    var isExchange = false
        set(value) {
            field = value
            notify(this)
        }
    fun getTotalWeight():Double{
        return currentAmount *weight
    }
    override fun getType(): String {
        return ViewType.TrideItemsViewModel
    }

    override fun onRegister() {

    }

    override fun onUnRegister() {
    }

    override fun onCloseObserved() {
        adapter.clear()
    }
}