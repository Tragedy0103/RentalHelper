package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.ViewModels.IType.MyOrderPageViewModel
import com.example.RentHelper.ViewModels.IType.MyStorePageViewModel
import com.example.RentHelper.ViewModels.MyStoreFragmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyStoreFragment : Fragment(), Observer<MyStoreFragmentViewModel> {
    var myStoreFragmentViewModel: MyStoreFragmentViewModel? = null
    var tlMyStore: TabLayout? = null
    var vpProductInformation: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myStoreFragmentViewModel = MyStoreFragmentViewModel()
        myStoreFragmentViewModel?.addItem(MyStorePageViewModel("OnShelf"))
            ?.addItem(MyStorePageViewModel("OffShelf"))
            ?.addItem(MyOrderPageViewModel("已立單"))
            ?.addItem(MyOrderPageViewModel("已寄送"))
            ?.addItem(MyOrderPageViewModel("已抵達"))
            ?.addItem(MyOrderPageViewModel("歸還已寄出"))
            ?.addItem(MyOrderPageViewModel("已結單"))
        onBind(myStoreFragmentViewModel!!)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_gameshop_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.findFragmentByTag("Title")?.view?.findViewById<TextView>(R.id.tvTitle)?.text =
            "我的賣場"
        tlMyStore = view.findViewById(R.id.tlMain)
        vpProductInformation = view.findViewById(R.id.vpProductInformation)
        vpProductInformation?.adapter = myStoreFragmentViewModel?.adapter
        TabLayoutMediator(tlMyStore!!, vpProductInformation!!) { tab, position ->
            when (position) {
                0 -> tab.text = "上架中"
                1 -> tab.text = "未上架"
                2 -> tab.text = "已下單"
                3 -> tab.text = "寄送中"
                4 -> tab.text = "租借中"
                5 -> tab.text = "歸還中"
                6 -> tab.text = "歷史紀錄"
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        myStoreFragmentViewModel?.updata()
    }
    override fun onDestroy() {
        super.onDestroy()
        myStoreFragmentViewModel = null
        tlMyStore = null
        vpProductInformation = null
    }

    override fun onBind(observed: Observed<MyStoreFragmentViewModel>) {
        observed.register(this)
    }

    override fun update(data: MyStoreFragmentViewModel) {

    }
}