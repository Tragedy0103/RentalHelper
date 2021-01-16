package com.example.RentHelper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.RentHelper.Fragment.CartMainFragment
import com.example.RentHelper.Fragment.CartTitleFragment
import com.example.RentHelper.R

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportFragmentManager.beginTransaction()
            .replace(R.id.llTitle,CartTitleFragment(),"CartTitle")
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.llMain,CartMainFragment(),"CartMain")
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}