package com.example.RentHelper.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.Tool.BaseViewHolder
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.IType.MainNewsCardViewModel


class MainNewsViewHolder(itemView: View) : BaseViewHolder<MainNewsCardViewModel>(itemView) {

    val rcvNewsInformation = itemView.findViewById<RecyclerView>(R.id.rcvNewsInformation)


    override fun bind(item: MainNewsCardViewModel) {
        rcvNewsInformation.adapter = item.adapter
        rcvNewsInformation.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        itemView.findViewById<TextView>(R.id.tvNewsInformation).text = item.title
        itemView.findViewById<TextView>(R.id.tvNewsSeeMore).setOnClickListener {
            item.seeMore?.invoke()
        }
    }
}