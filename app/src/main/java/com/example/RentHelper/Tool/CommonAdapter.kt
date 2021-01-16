package com.example.RentHelper.Tool

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import java.util.HashMap

class CommonAdapter private constructor(
    private val factories: List<ViewHolderBuilder>,// 利用工廠降低耦合, 避免直接傳入實體的mapping方式
    private val typeMap: Map<String, Int>,// mapping item與viewholder
    private var items: MutableList<IType>,
    private var header: IType? = null,
    private var footer: IType? = null
) : RecyclerView.Adapter<BaseViewHolder<out IType>>() {

    companion object {
        private const val TYPE_HEADER = "Header"// 無須被知道的鍵值 僅用做內部Mapping處理
        private const val TYPE_FOOTER = "footer"// 無須被知道的鍵值 僅用做內部Mapping處理
    }

    class Builder(private var items: MutableList<IType>) {
        // 利用builder配合lambda提高開發流暢度，鍊式調用較為友善
        private val factories: MutableList<ViewHolderBuilder>
        private val typeMap: MutableMap<String, Int>
        private var header: IType? = null
        private var footer: IType? = null

        init {
            factories = arrayListOf()
            typeMap = HashMap()
        }

        fun addType(factory: ViewHolderBuilder, type: String): Builder {
            factories.add(factory)
            typeMap[type] = factories.size - 1
            return this
        }

        fun addHeader(factory: ViewHolderBuilder): Builder {
            typeMap[TYPE_HEADER] ?: let {
                factories.add(factory)
                typeMap[TYPE_HEADER] = factories.size - 1
                header = object : IType {
                    override fun getType(): String {
                        return TYPE_HEADER
                    }
                }
            }
            return this
        }

        fun addFooter(factory: ViewHolderBuilder): Builder {
            typeMap[TYPE_FOOTER] ?: let {
                factories.add(factory)
                typeMap[TYPE_FOOTER] = factories.size - 1
                footer = object : IType {
                    override fun getType(): String {
                        return TYPE_FOOTER
                    }
                }
            }
            return this
        }

        fun build(): CommonAdapter = CommonAdapter(factories, typeMap, items, header, footer)
    }

    var onBind: ((position: Int) -> Unit)? = null
    override fun getItemViewType(position: Int): Int {
        header?.let {
            if (position == 0)
                return typeMap[TYPE_HEADER] ?: error("Type Not Found")
        }
        footer?.let {
            if (position == itemCount - 1)
                return typeMap[TYPE_FOOTER] ?: error("Type Not Found")
        }
        return typeMap[items[position - (if (header != null) 1 else 0)].getType()]
            ?: error("Type Not Found")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseViewHolder<out IType> =
        factories[i].invoke(viewGroup)

    override fun onBindViewHolder(holder: BaseViewHolder<out IType>, position: Int) {
        onBind?.invoke(position)
        header?.let {
            if (position == 0)
                return
        }
        footer?.let {
            if (position == itemCount - 1)
                return
        }
        // 隱含轉換
        (holder as BaseViewHolder<IType>).bind(items[position - (if (header != null) 1 else 0)])
    }

    override fun onViewRecycled(holder: BaseViewHolder<out IType>) = holder.onViewRecycled()

    fun getItemSize():Int{
        return items.size
    }

    override fun getItemCount(): Int =
        items.size + (if (header != null) 1 else 0) + if (footer != null) 1 else 0

    /* data binding */
    fun bind(items: MutableList<IType>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun add(item: IType?) {
        item ?: return
        this.items.add(item)
        notifyItemInserted(itemCount - 1 - if (footer != null) 1 else 0)
    }

    fun add(items: List<IType>?) {
        items ?: return
        this.items.addAll(items)
        notifyItemRangeInserted(itemCount - items.size - if (footer != null) 1 else 0, items.size)
    }

    fun add(index: Int, item: IType?) {
        Log.d("LoadProduct", "add$index")
        item ?: return
        this.items.add(index, item)
        notifyItemInserted(index + if (header != null) 1 else 0)
    }

    fun remove(position: Int) {
        Log.d("LoadProduct", "remove$position")
        if (position in 0 until itemCount) {
            this.items.removeAt(position)
            notifyItemRemoved(position + if (header != null) 1 else 0)
        }
    }

    fun remove(items: IType) {
        val position: Int = this.items.indexOf(items)
        this.items.removeAt(position)
        notifyItemRemoved(position + if (header != null) 1 else 0)
    }

    fun clear() {
        val count = itemCount
        items.forEach {
            if (it is Observed<*>) {
                it.closeObserved()
            }
        }
        this.items.clear()
        notifyItemRangeRemoved(0, count)
    }

    fun map(action: (itype: IType) -> Unit) {
        items.forEach {
            action.invoke(it)
        }
    }
}
