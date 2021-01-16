package com.example.RentHelper.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.ViewModels.GameShopFragmentViewModel
import com.example.RentHelper.ViewModels.IType.GameShopPageViewModel
import com.example.RentHelper.ViewModels.MainTitleFragmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GameShopFragment : Fragment(), Observer<GameShopFragmentViewModel> {
    var gameShopFragmentViewModel: GameShopFragmentViewModel? = null
    var tlMain: TabLayout? = null
    var vpProductInformation: ViewPager2? = null
    var type1: String? = null
    var type2: String? = null

    companion object {
        fun newInstance(
            type1: String,
            type2: String
        ): GameShopFragment {
            val fragment = GameShopFragment()
            fragment.arguments = Bundle().apply {
                putString("type1", type1)
                putString("type2", type2)
            }
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameShopFragmentViewModel = GameShopFragmentViewModel()
        type1 = arguments?.getString("type1")
        type2 = arguments?.getString("type2")
        type1?.let { type1 ->
            MainTitleFragmentViewModel.instance.title=type1
            when (type1) {
                "桌遊" -> {
                    type2?.let { type2 ->
                        if(type2!="All"){
                            MainTitleFragmentViewModel.instance.title=type2
                        }
                        gameShopFragmentViewModel?.addItem(
                            GameShopPageViewModel(
                                type1,
                                type2,
                                "All"
                            )
                        )
                            ?.addItem(GameShopPageViewModel(type1, type2, "策略"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "友情破壞"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "技巧"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "經營"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "運氣"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "劇情"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "TRPG"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "其他"))
                        onBind(gameShopFragmentViewModel!!)
                    }
                }
                else -> {
                    type2?.let { type2 ->
                        if(type2!="All"){
                            MainTitleFragmentViewModel.instance.title=type2
                        }
                        gameShopFragmentViewModel?.addItem(
                            GameShopPageViewModel(
                                type1,
                                type2,
                                "All"
                            )
                        )
                            ?.addItem(GameShopPageViewModel(type1, type2, "遊戲"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "主機"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "週邊"))
                            ?.addItem(GameShopPageViewModel(type1, type2, "其他"))
                        onBind(gameShopFragmentViewModel!!)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gameshop_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlMain = view.findViewById(R.id.tlMain)
        vpProductInformation = view.findViewById(R.id.vpProductInformation)
        vpProductInformation?.adapter = gameShopFragmentViewModel?.adapter
        TabLayoutMediator(tlMain!!, vpProductInformation!!) { tab, position ->
            when (type1) {
                "桌遊" -> {
                    when (position) {
                        0 -> tab.text = "所有"
                        1 -> tab.text = "策略"
                        2 -> tab.text = "友情破壞"
                        3 -> tab.text = "技巧"
                        4 -> tab.text = "經營"
                        5 -> tab.text = "運氣"
                        6 -> tab.text = "劇情"
                        7 -> tab.text = "TRPG"
                        8 -> tab.text = "其他"
                    }
                }
                else -> {
                    when (position) {
                        0 -> tab.text = "所有"
                        1 -> tab.text = "遊戲"
                        2 -> tab.text = "主機"
                        3 -> tab.text = "週邊"
                        4 -> tab.text = "其他"
                    }
                }
            }

        }.attach()
    }
    override fun onResume() {
        super.onResume()
        gameShopFragmentViewModel?.updata()

    }
    override fun onBind(observed: Observed<GameShopFragmentViewModel>) {
        observed.register(this)
    }

    override fun update(data: GameShopFragmentViewModel) {
    }
}