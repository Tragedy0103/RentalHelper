package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.Activity.MainActivity
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Router
import com.example.RentHelper.ViewModels.MenuFragmentViewModel
import com.example.RentHelper.ViewModels.IType.MenuItemViewModel
import com.example.RentHelper.ViewModels.MainTitleFragmentViewModel


class MenuFragment : Fragment() {
    lateinit var menuFragmentViewModel: MenuFragmentViewModel
    lateinit var tvHeader: TextView
    lateinit var imgIcon: ImageView
    lateinit var tvOrderList: TextView
    lateinit var tvWishList: TextView
    lateinit var tvHome: TextView
    lateinit var rcvMenu: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        menuFragmentViewModel = MenuFragmentViewModel()
        tvHeader = view.findViewById(R.id.tvHeader)
        imgIcon = view.findViewById(R.id.imgIcon)
        tvOrderList = view.findViewById(R.id.tvOrderList)
        tvWishList = view.findViewById(R.id.tvWishList)
        tvHome = view.findViewById(R.id.tvHome)
        rcvMenu = view.findViewById(R.id.rcvMenu)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val home :(v:View)->Unit= {
            MainTitleFragmentViewModel.instance.title ="租之助"
            Router.router(
                Router.NextTmp(
                    context!!,
                    MainFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "tag",
                    true
                )
            )
            activity?.findViewById<DrawerLayout>(R.id.dlMain)
                ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)}
        imgIcon.setOnClickListener(home)
        tvHome.setOnClickListener (home)
        tvHeader.setOnClickListener {
            MainTitleFragmentViewModel.instance.title ="我的賣場"
            (context as MainActivity).nextTmp = Router.router(
                Router.NextTmp(
                    this.context!!,
                    MyStoreFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "tag",
                    true
                )
            )
            activity?.findViewById<DrawerLayout>(R.id.dlMain)
                ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
        }
        tvOrderList.setOnClickListener {
            MainTitleFragmentViewModel.instance.title ="我的訂單"
            (context as MainActivity).nextTmp = Router.router(
                Router.NextTmp(
                    this.context!!,
                    OrderListFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "tag",
                    true
                )
            )
            activity?.findViewById<DrawerLayout>(R.id.dlMain)
                ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
        }
        tvWishList.setOnClickListener {
            MainTitleFragmentViewModel.instance.title ="願望清單"
            (context as MainActivity).nextTmp = Router.router(
                Router.NextTmp(
                    this.context!!,
                    WishListFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "tag",
                    true
                )
            )
            activity?.findViewById<DrawerLayout>(R.id.dlMain)
                ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
        }
    }

    override fun onStart() {
        super.onStart()
        menuFragmentViewModel.addItem(
            MenuItemViewModel("PlayStation")
                .addItems("PS5") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("PlayStation", "PS5"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
                .addItems("PS4") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("PlayStation", "PS4"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
        ).addItem(
            MenuItemViewModel("XBox")
                .addItems("Series") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("XBox", "Series"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
                .addItems("One") {
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.llMain, GameShopFragment.newInstance("XBox", "One"), "Shop")
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
        ).addItem(
            MenuItemViewModel("Switch")
                .addItems("Switch") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("Switch", "Switch"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
        ).addItem(
            MenuItemViewModel("桌遊")
                .addItems("4人以下") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("桌遊", "4人以下"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
                .addItems("4-8人") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("桌遊", "4-8人"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
                .addItems("8人以上") {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.llMain,
                            GameShopFragment.newInstance("桌遊", "8人以上"),
                            "Shop"
                        )
                        ?.addToBackStack(null)
                        ?.commit()
                    activity?.findViewById<DrawerLayout>(R.id.dlMain)
                        ?.closeDrawer(activity?.findViewById<LinearLayout>(R.id.llMenu)!!)
                }
        )
        rcvMenu.adapter = menuFragmentViewModel.adapter
        rcvMenu.layoutManager = LinearLayoutManager(view?.context)

    }

    override fun onStop() {
        super.onStop()
        menuFragmentViewModel.adapter.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}