package com.example.RentHelper.ViewModels

import com.example.RentHelper.Tool.Observed

class MainTitleFragmentViewModel : Observed<MainTitleFragmentViewModel>() {

    companion object{
        val instance:MainTitleFragmentViewModel by lazy {
            MainTitleFragmentViewModel()
         }
    }

    var title: String? = "租之助"
        set(value) {
            field = value
            notify(this)
        }
    var menuIconAction: (() -> Unit)? = null
        set(value) {
            field = value
            notify(this)
        }
    var notifyIconAction: (() -> Unit)? = null
        set(value) {
            field = value
            notify(this)
        }
    var cartIconAction: (() -> Unit)? = null
        set(value) {
            field = value
            notify(this)
        }

    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {
    }
}