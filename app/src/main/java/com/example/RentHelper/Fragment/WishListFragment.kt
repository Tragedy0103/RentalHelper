package com.example.RentHelper.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.ViewModels.WishListViewModel

class WishListFragment : Fragment(), Observer<WishListViewModel> {
    var rcvWishList: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onBind(WishListViewModel.instance)
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)
        rcvWishList = view.findViewById(R.id.rcvWishList)
        rcvWishList?.layoutManager = LinearLayoutManager(this.context)
        rcvWishList?.adapter = WishListViewModel.instance.adapter
        WishListViewModel.instance.onAdd={str->
            Toast.makeText(this.context,str,Toast.LENGTH_SHORT).show()
        }
        WishListViewModel.instance.onDelete={str->
            Toast.makeText(this.context,str,Toast.LENGTH_SHORT).show()
        }
        WishListViewModel.instance.onDeleteImgClickAction = { item ->
            var dialog: Dialog? = null
            val dialogView =
                LayoutInflater.from(this.context!!)
                    .inflate(R.layout.item_dialog_isdelete, null, false)
            dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
                WishListViewModel.instance.deleteItems(item)
                dialog?.hide()
            }
            dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog?.hide()
            }
            dialog = AlertDialog.Builder(this.context!!)
                .setView(dialogView)
                .create()
            dialog.show()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        WishListViewModel.instance.upData()
    }
    override fun onDestroy() {
        super.onDestroy()
        WishListViewModel.instance.closeObserved()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onBind(observed: Observed<WishListViewModel>) {
        observed.register(this)
    }

    override fun update(data: WishListViewModel) {
        rcvWishList?.adapter = data.adapter
    }
}
