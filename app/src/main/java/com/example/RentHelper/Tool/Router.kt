package com.example.RentHelper.Tool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.RentHelper.Activity.*
import com.example.RentHelper.Fragment.*
import com.example.RentHelper.R
import com.example.RentHelper.models.AccountModel

object Router {
    class NextTmp() {
        constructor(currentContext: Context, tmpNextCls: Class<*>, bundle: Bundle?) : this() {
            this.currentContext = currentContext
            this.nextCls = tmpNextCls
            this.bundle = bundle
        }

        constructor(
            currentContext: Context,
            tmpNextFragment: Fragment,
            resourceId: Int?,
            fragmentManager: FragmentManager,
            tag: String,
            isBackToStack: Boolean
        ) : this() {
            this.currentContext = currentContext
            this.nextFragment = tmpNextFragment
            this.resourceId = resourceId
            this.fragmentManager = fragmentManager
            this.tag = tag
        }

        var currentContext: Context? = null
        var nextFragment: Fragment? = null
        var resourceId: Int? = null
        var fragmentManager: FragmentManager? = null
        var tag: String? = null
        var nextCls: Class<*>? = null
        var bundle: Bundle? = null
        var isBackToStack: Boolean = true
    }

    fun router(nextTmp: NextTmp): NextTmp? {
        return when {
            nextTmp.nextCls != null -> {
                when (nextTmp.nextCls) {
                    CartActivity::class.java,
                    AddProductActivity::class.java,
                    ModifyProductActivity::class.java,
                    OrderActivity::class.java -> {
                        isActivityLogin(nextTmp)
                    }
                    ProductActivity::class.java -> {
                        changeActivity(nextTmp.currentContext!!, nextTmp.nextCls!!, nextTmp.bundle)
                    }

                    else -> null
                }
            }
            else -> {
                when (nextTmp.nextFragment) {
                    is MyStoreFragment,
                    is AccountFragment,
                    is AccountInfoFragment,
                    is WishListFragment -> {
                        isFragmentLogin(nextTmp)
                    }
                    else -> {
                        changeFragment(
                            nextTmp.fragmentManager!!,
                            nextTmp.resourceId!!,
                            nextTmp.nextFragment!!,
                            nextTmp.tag,
                            nextTmp.isBackToStack
                        )
                    }
                }
            }
        }
    }

    private fun changeFragment(
        fragmentManager: FragmentManager,
        resourceId: Int,
        fragment: Fragment,
        tag: String?,
        isBackToStack: Boolean
    ): NextTmp? {
        if (isBackToStack) {
            fragmentManager.beginTransaction()
                .replace(resourceId, fragment, tag)
                .addToBackStack(null)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .replace(resourceId, fragment, tag)
                .commit()
        }
        return null
    }

    fun changeActivity(
        currentContext: Context,
        nextCls: Class<*>,
        bundle: Bundle?
    ): NextTmp? {
        val intent = Intent(currentContext, nextCls)
        bundle?.let { intent.putExtras(it) }
        ContextCompat.startActivity(
            currentContext, intent, null
        )
        return null
    }

    fun isLogin(context: Context) {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_dialog_login, null, false)
        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()
        view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
            changeActivity(context, LoginActivity::class.java, null)
            dialog.hide()
        }
        view.findViewById<Button>(R.id.btnNo)?.setOnClickListener {
            dialog.hide()
        }
        when (AccountModel.INSTANCE.stage) {
            AccountModel.Stage.LOGIN -> {

            }
            AccountModel.Stage.UNLOGIN -> {
                dialog.show()
            }
        }
    }

    private fun isFragmentLogin(nextTmp: NextTmp): NextTmp? {
        var tmp: NextTmp? = nextTmp
        val view =
            LayoutInflater.from(nextTmp.currentContext)
                .inflate(R.layout.item_dialog_login, null, false)
        val dialog = AlertDialog.Builder(nextTmp.currentContext!!)
            .setView(view)
            .create()
        view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
            changeActivity(nextTmp.currentContext!!, LoginActivity::class.java, null)
            dialog.hide()
        }
        view.findViewById<Button>(R.id.btnNo)?.setOnClickListener {
            tmp = null
            dialog.hide()
        }
        return when (AccountModel.INSTANCE.stage) {
            AccountModel.Stage.LOGIN -> {
                changeFragment(
                    nextTmp.fragmentManager!!,
                    nextTmp.resourceId!!,
                    nextTmp.nextFragment!!,
                    nextTmp.tag,
                    nextTmp.isBackToStack
                )
            }
            AccountModel.Stage.UNLOGIN -> {
                dialog.show()
                tmp
            }
        }
    }

    private fun isActivityLogin(nextTmp: NextTmp): NextTmp? {
        var tmp: NextTmp? = nextTmp
        val view =
            LayoutInflater.from(nextTmp.currentContext)
                .inflate(R.layout.item_dialog_login, null, false)
        val dialog = AlertDialog.Builder(nextTmp.currentContext!!)
            .setView(view)
            .create()
        view.findViewById<Button>(R.id.btnYes)?.setOnClickListener {
            changeActivity(nextTmp.currentContext!!, LoginActivity::class.java, null)
            dialog.hide()
        }
        view.findViewById<Button>(R.id.btnNo)?.setOnClickListener {
            tmp = null
            dialog.hide()
        }
        return when (AccountModel.INSTANCE.stage) {
            AccountModel.Stage.LOGIN -> {
                changeActivity(nextTmp.currentContext!!, nextTmp.nextCls!!, nextTmp.bundle)
            }
            AccountModel.Stage.UNLOGIN -> {
                dialog.show()
                tmp
            }
        }
    }

}