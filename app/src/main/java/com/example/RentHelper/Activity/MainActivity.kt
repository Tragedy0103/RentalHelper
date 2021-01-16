package com.example.RentHelper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.example.RentHelper.*
import com.example.RentHelper.Fragment.*
import com.example.RentHelper.Tool.Router
import com.example.RentHelper.ViewModels.MainTitleFragmentViewModel
import com.example.RentHelper.models.AccountModel
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    var nextTmp: Router.NextTmp? = null

    private fun pop() {
        nextTmp = null
    }

    override fun onResume() {
        super.onResume()
        nextTmp?.let { Router.router(it) }
        pop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val email: String = sharedPreferences.getString("email", "")!!
        val password: String = sharedPreferences.getString("password", "")!!

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (email != "" && password != "") {
                AccountModel.INSTANCE.login(email, password,it.result!!)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.llTitle, MainTitleFragment(), "Title")
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.llMenu, MenuFragment(), "Menu")
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.llMain,
                MainFragment(), "Main"
            )
            .commit()
    }

    override fun onStart() {
        super.onStart()
        val dlMain = findViewById<DrawerLayout>(R.id.dlMain)
        val llMenu = findViewById<LinearLayout>(R.id.llMenu)
        MainTitleFragmentViewModel.instance.menuIconAction = {
            dlMain.openDrawer(llMenu)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.fragments.clear()
    }
}
