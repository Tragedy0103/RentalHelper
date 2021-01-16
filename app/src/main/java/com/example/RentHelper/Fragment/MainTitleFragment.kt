package com.example.RentHelper.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.RentHelper.Activity.CartActivity
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.Tool.Router
import com.example.RentHelper.ViewModels.MainTitleFragmentViewModel


class MainTitleFragment: Fragment(),Observer<MainTitleFragmentViewModel> {
    var imgMenu :ImageView?=null
    var tvTitle :TextView?= null
    var imgCart :ImageView?=null
    var imgNotify :ImageView?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) :View {
        val view  =inflater.inflate(R.layout.fragment_main_title, container, false)
        onBind(MainTitleFragmentViewModel.instance)
        imgMenu=view.findViewById(R.id.imgMenu)
        tvTitle =view.findViewById(R.id.tvTitle)
        imgCart = view.findViewById(R.id.imgCart)
        imgNotify= view.findViewById(R.id.imgNotification)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update(MainTitleFragmentViewModel.instance)
        MainTitleFragmentViewModel.instance.cartIconAction = {
            Router.router(Router.NextTmp(this.context!!,CartActivity::class.java,null))
        }
        MainTitleFragmentViewModel.instance.notifyIconAction ={
            if(fragmentManager?.fragments?.last()!=AccountFragment.instance)
            Router.router(Router.NextTmp(this.context!!,AccountInfoFragment(),R.id.llMain,fragmentManager!!,"account",
                false))
        }
    }

    override fun onBind(observed: Observed<MainTitleFragmentViewModel>) {
        observed.register(this)
    }

    override fun update(data: MainTitleFragmentViewModel) {
        tvTitle?.text =data.title
        imgMenu?.setOnClickListener {
            data.menuIconAction?.invoke()
        }
        imgCart?.setOnClickListener {
            data.cartIconAction?.invoke()
        }
        imgNotify?.setOnClickListener {
            data.notifyIconAction?.invoke()
        }
    }
}