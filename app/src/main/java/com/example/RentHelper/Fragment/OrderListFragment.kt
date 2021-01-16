package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.AccountOrderViewModel
import com.example.RentHelper.ViewModels.IType.MyOrderPageViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderListFragment : Fragment(R.layout.fragment_gameshop_main) {
    var accountOrderViewModel: AccountOrderViewModel? = null
    var tlMain: TabLayout? = null
    var vpProductInformation: ViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountOrderViewModel = AccountOrderViewModel()
        accountOrderViewModel?.apply {
            addItem(MyOrderPageViewModel("All"))
            addItem(MyOrderPageViewModel("已立單"))
            addItem(MyOrderPageViewModel("已寄送"))
            addItem(MyOrderPageViewModel("已抵達"))
            addItem(MyOrderPageViewModel("歸還已寄出"))
            addItem(MyOrderPageViewModel("已歸還"))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlMain = view.findViewById(R.id.tlMain)
        vpProductInformation = view.findViewById(R.id.vpProductInformation)
        vpProductInformation?.adapter = accountOrderViewModel?.adapter
        TabLayoutMediator(tlMain!!, vpProductInformation!!) { tab, position ->
            when (position) {
                0 -> tab.text = "所有"
                1 -> tab.text = "已立單"
                2 -> tab.text = "寄送中"
                3 -> tab.text = "已抵達"
                4 -> tab.text = "歸還已寄出"
                5 -> tab.text = "已歸還"
            }
        }.attach()
    }
}