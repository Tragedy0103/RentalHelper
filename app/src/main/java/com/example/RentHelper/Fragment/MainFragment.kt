package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.ViewModels.IType.MainFragmentViewModel
import com.example.RentHelper.ViewModels.IType.MainNewsCardViewModel
import com.example.RentHelper.ViewModels.MainTitleFragmentViewModel


class MainFragment : Fragment(), Observer<MainFragmentViewModel> {
    private val mainFragmentViewModel = MainFragmentViewModel()
    private lateinit var rcvMain: RecyclerView
    private var psMainNewsCardViewModel: MainNewsCardViewModel? = null
    private var xboxMainNewsCardViewModel: MainNewsCardViewModel? = null
    private var switchMainNewsCardViewModel: MainNewsCardViewModel? = null
    private var otherMainNewsCardViewModel: MainNewsCardViewModel? = null
    private var boardGameMainNewsCardViewModel: MainNewsCardViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        rcvMain = view.findViewById(R.id.rcvMain)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind(mainFragmentViewModel)

    }

    override fun onResume() {
        super.onResume()
        fragmentManager?.let {
            for (i in 0 until it.backStackEntryCount) {
                it.popBackStack()
            }
        }
        MainTitleFragmentViewModel.instance.title = "租之助"
        psMainNewsCardViewModel = MainNewsCardViewModel(getString(R.string.PlaystationNewsInfo))
            .setSeeMore {
                fragmentManager?.beginTransaction()
                    ?.replace(
                        R.id.llMain,
                        GameShopFragment.newInstance("PlayStation", "All"),
                        "News"
                    )
                    ?.addToBackStack(null)
                    ?.commit()
            }
        xboxMainNewsCardViewModel = MainNewsCardViewModel(getString(R.string.XBoxNewsInfo))
            .setSeeMore {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.llMain, GameShopFragment.newInstance("XBox", "All"), "News")
                    ?.addToBackStack(null)
                    ?.commit()
            }
        switchMainNewsCardViewModel= MainNewsCardViewModel(getString(R.string.SwitchNewsInfo))
            .setSeeMore {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.llMain, GameShopFragment.newInstance("Switch", "All"), "News")
                    ?.addToBackStack(null)
                    ?.commit()
            }
        otherMainNewsCardViewModel= MainNewsCardViewModel(getString(R.string.OtherNewsInfo))
            .setSeeMore {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.llMain, GameShopFragment.newInstance("Other", "All"), "News")
                    ?.addToBackStack(null)
                    ?.commit()
            }
        boardGameMainNewsCardViewModel= MainNewsCardViewModel("桌遊")
            .setSeeMore {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.llMain, GameShopFragment.newInstance("桌遊", "All"), "News")
                    ?.addToBackStack(null)
                    ?.commit()
            }
        mainFragmentViewModel.addItem(psMainNewsCardViewModel!!)
            .addItem(xboxMainNewsCardViewModel!!)
            .addItem(switchMainNewsCardViewModel!!)
            .addItem(otherMainNewsCardViewModel!!)
            .addItem(boardGameMainNewsCardViewModel!!)

        rcvMain.adapter = mainFragmentViewModel.adapter
        rcvMain.layoutManager =LinearLayoutManager(view!!.context)
    }
    override fun onPause() {
        super.onPause()
        mainFragmentViewModel.adapter.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainFragmentViewModel.closeObserved()
    }

    override fun update(data: MainFragmentViewModel) {
    }

    override fun onBind(observed: Observed<MainFragmentViewModel>) {
        observed.register(this)
    }
}