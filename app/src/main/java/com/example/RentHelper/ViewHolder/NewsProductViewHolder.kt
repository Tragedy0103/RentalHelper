package com.example.RentHelper.ViewHolder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.RentHelper.Activity.ProductActivity
import com.example.RentHelper.Tool.*
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.IType.ProductCardViewModel
import com.facebook.drawee.view.SimpleDraweeView

class NewsProductViewHolder(itemView: View) : BaseViewHolder<ProductCardViewModel>(itemView) {
    override fun bind(item: ProductCardViewModel) {
        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("ProductId", item.id)
            startActivity(
                itemView.context, Intent(
                    Intent(
                        itemView.context,
                        ProductActivity::class.java
                    )
                ).putExtras(bundle), null
            )
        }
        item.uri?.let {
            itemView.findViewById<SimpleDraweeView>(R.id.imgProduct).setImageURI(it,itemView.context)
        }

        itemView.findViewById<TextView>(R.id.tvProductName).text = item.title

        val llProductType = itemView.findViewById<LinearLayout>(R.id.llProductType)
        llProductType.removeAllViews()
        item.saleType.forEach {str->
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.item_tab_text,llProductType,false)
            view.findViewById<TextView>(R.id.tvTab).text = str
            llProductType.addView(view)
        }
    }
}
