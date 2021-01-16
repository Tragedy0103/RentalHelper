package com.example.RentHelper.Fragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.CartMainFragmentViewModel

class CartMainFragment : Fragment() {

    val cartMainFragmentViewModel = CartMainFragmentViewModel()
    override fun onResume() {
        super.onResume()
        cartMainFragmentViewModel.upDataList()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cart_main, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rcvCartProducts = view.findViewById<RecyclerView>(R.id.rcvCartProducts)
        rcvCartProducts.adapter = cartMainFragmentViewModel.adapter
        rcvCartProducts.layoutManager = LinearLayoutManager(view.context)
    }
}