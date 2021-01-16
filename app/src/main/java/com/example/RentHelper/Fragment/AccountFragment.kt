package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.Tool.Router
import com.example.RentHelper.ViewModels.IType.AccountListItemViewModel
import com.example.RentHelper.ViewModels.AccountViewModel
import com.example.RentHelper.models.AccountModel

class AccountFragment : Fragment(), Observer<AccountViewModel> {
    companion object{
        val instance:AccountFragment by lazy { AccountFragment() }
    }
    var rcvMenu: RecyclerView? = null
    var accountViewModel: AccountViewModel? = AccountViewModel()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBind(accountViewModel!!)
        accountViewModel?.adapter?.clear()
        accountViewModel?.addItem(AccountListItemViewModel("帳號資訊").setAction {
            Router.router(
                Router.NextTmp(
                    this.context!!,
                    AccountInfoFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "account",
                    true
                )
            )
        })
        accountViewModel?.addItem(AccountListItemViewModel("訂單資訊").setAction {
            Router.router(
                Router.NextTmp(
                    this.context!!,
                    OrderListFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "orderList",
                    true
                )
            )
        })
        accountViewModel?.addItem(AccountListItemViewModel("願望清單").setAction {
            Router.router(
                Router.NextTmp(
                    this.context!!,
                    WishListFragment(),
                    R.id.llMain,
                    fragmentManager!!,
                    "wishList",
                    true
                )
            )
        })
        accountViewModel?.addItem(AccountListItemViewModel("會員登出").setAction {
            context?.let { it ->
                val view =
                    LayoutInflater.from(it)
                        .inflate(R.layout.item_dialog_login, null, false)
                val dialog = AlertDialog.Builder(it)
                    .setView(view)
                    .create()
                view.findViewById<TextView>(R.id.tvtitle).text = "是否登出?"
                view.findViewById<TextView>(R.id.tvIsLogin).text = ""
                view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
                    it.context.getSharedPreferences(
                        "User",
                        MODE_PRIVATE
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
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_main, container, false)
        rcvMenu = view?.findViewById(R.id.rcvMenu)
        rcvMenu?.adapter = accountViewModel?.adapter
        rcvMenu?.layoutManager = LinearLayoutManager(this.context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        accountViewModel?.closeObserved()
    }

    override fun onBind(observed: Observed<AccountViewModel>) {
        observed.register(this)
    }

    override fun update(data: AccountViewModel) {

    }
}