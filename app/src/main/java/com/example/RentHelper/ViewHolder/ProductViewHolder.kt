package com.example.RentHelper.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.Tool.BaseViewHolder
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.IType.MainNewsCardViewModel

class ProductViewHolder(itemView: View) : BaseViewHolder<MainNewsCardViewModel>(itemView) {
    val rcvNewsInformation = itemView.findViewById<RecyclerView>(R.id.rcvNewsInformation)
    override fun bind(item: MainNewsCardViewModel) {
        itemView.findViewById<TextView>(R.id.tvNewsInformation).text = item.title
        rcvNewsInformation.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        rcvNewsInformation.adapter = item.adapter
        itemView.findViewById<TextView>(R.id.tvNewsSeeMore).text=""
    }
}