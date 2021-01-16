package com.example.RentHelper.ViewHolder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.RentHelper.Activity.MainActivity
import com.example.RentHelper.Fragment.GameShopFragment
import com.example.RentHelper.Tool.*
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.IType.MenuItemTextViewModel
import com.example.RentHelper.ViewModels.IType.MenuItemViewModel

class MenuViewHolder(itemView: View) : BaseViewHolder<MenuItemViewModel>(itemView) {
    override fun bind(item: MenuItemViewModel) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = item.title

        tvTitle.setOnClickListener {
            (itemView.context as MainActivity).apply {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.llMain,
                        GameShopFragment.newInstance(item.title, "All"),
                        "Shop"
                    )
                    .addToBackStack(null)
                    .commit()
                findViewById<DrawerLayout>(R.id.dlMain)?.closeDrawer(
                    this.findViewById<LinearLayout>(R.id.llMenu)
                )
            }
        }
        val llMenu = itemView.findViewById<LinearLayout>(R.id.llMenu)
        item.items.forEach { itype ->
            (itype as MenuItemTextViewModel)
            val view = LayoutInflater.from(itemView.context)
                .inflate(R.layout.item_list_menu_text, llMenu, false)
            view.findViewById<TextView>(R.id.tvMenuElement).run {
                text = itype.text
                paint.flags = Paint.UNDERLINE_TEXT_FLAG; //下划线
                paint.isAntiAlias = true;//抗锯齿
            }
            view.setOnClickListener {
                itype.action.invoke()
            }
            llMenu.addView(view)
        }
    }
}