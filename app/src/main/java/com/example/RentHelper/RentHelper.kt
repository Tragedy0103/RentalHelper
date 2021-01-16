package com.example.RentHelper

import android.app.Application
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class RentHelper: Application() {
    override fun onCreate() {
        super.onCreate()



        Fresco.initialize(this)
        //推播-取得推播憑證
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            //如果沒成功，印錯誤資訊返回不做事
            if (!task.isSuccessful) {
                Log.w("FirebaseMessaging", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val registerId = task.result

            // Log
            Log.d("FirebaseMessaging", registerId!!)
            //取得使用者資料\
        })

    }
}