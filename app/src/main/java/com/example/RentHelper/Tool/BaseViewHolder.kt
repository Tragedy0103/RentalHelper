package com.example.RentHelper.Tool

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

typealias ViewHolderBuilder = (viewGroup: ViewGroup) -> BaseViewHolder<out IType>

abstract class BaseViewHolder<T : IType>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T)

    // 回收時調用處理接口
    open fun onViewRecycled() {}
}
