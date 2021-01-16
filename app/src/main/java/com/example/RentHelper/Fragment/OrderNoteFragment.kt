package com.example.RentHelper.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RentHelper.R
import com.example.RentHelper.ViewModels.OrderNoteFragmentViewModel

class OrderNoteFragment:Fragment() {
    companion object{
        fun newInstance (orderId:String):OrderNoteFragment{
            val fragment=OrderNoteFragment()
            val arg = Bundle()
            arg.putString("orderId",orderId)
            fragment.arguments = arg
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )=inflater.inflate(R.layout.fragment_order_note,container,false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString("orderId")

        val orderNoteFragmentViewModel=orderId?.let { OrderNoteFragmentViewModel(it) }
        val rcvMessage = view.findViewById<RecyclerView>(R.id.rcvMessage)
        rcvMessage.adapter = orderNoteFragmentViewModel?.adapter
        rcvMessage.layoutManager = LinearLayoutManager(this.context)
    }
}