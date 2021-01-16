package com.example.RentHelper.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.RentHelper.Activity.OrderActivity
import com.example.RentHelper.R
import com.example.RentHelper.models.AccountModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*

class FirebaseMessagingService : FirebaseMessagingService() {

    fun getMessage(jsonObject: JSONObject){

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //將從推播(後端)得到的資訊帶給首頁
        val noteType = remoteMessage.data["noteType"]
        val ProductTitle = remoteMessage.data["ProductTitle"]
        val orderId = remoteMessage.data["OrderId"]
        val sender = remoteMessage.data["Sender"]

        val bundle = Bundle().apply { putString("orderId",orderId) }
        when(noteType){
            "message"->{
                val message = remoteMessage.data["Message"]
                sendNotification(ProductTitle!!,
                    "$sender：$message",
                    bundle)
                remoteMessage.notification?.run{
                    sendNotification(title?:"", body?:"", bundle)
                }
            }
            "status"->{
                val newStatus = remoteMessage.data["newStatus"]
                val newStatusTime = remoteMessage.data["newStatusTime"]
                sendNotification(ProductTitle!!,
                    "訂單$newStatus       $newStatusTime",
                    bundle)
                remoteMessage.notification?.run{
                    sendNotification(title?:"", body?:"", bundle)
                }
            }
        }

    }
    override fun onCreate() {
        super.onCreate()
    }
    //拿到本機FCM的token
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            AccountModel.INSTANCE.deviceToken = token
        }

    }

    //通知欄UI
    private fun sendNotification(messageTitle :String, messageBody:String, bundle: Bundle){


        val intent = Intent(this, OrderActivity::class.java)
        //跳轉過去時將他以上(含)的activity都清除，重新創建
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtras(bundle)
        //點擊跳轉畫面，在被觸發時，會執行startActivity，如果是getService就是startService以此類推
        //OneShot: 此intent只能使用一次
        val pendingIntent =
            PendingIntent.getActivity(this, 0 ,intent, PendingIntent.FLAG_ONE_SHOT)
        //Android8以上 規定要設置channelId
        val channelId = "rent_helper_id"
        //取得預設鈴聲
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //通知欄 -設定抬頭、icon、字體顏色、內容、點選後將通知關閉、提示鈴聲、intent(點擊後的事件-跳去雷達首頁)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(messageTitle)
            .setSmallIcon(R.mipmap.ic_fcm_round)
            .setColor(getColor(R.color.ButtonColor))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        //取得系統通知服務(通知控制)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        //Oreo (Android8 = 26)以上版本需要channel Id
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "rent_radar_name",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        //將上面設定好的通知送給android，將訊息顯示在狀態欄上。
        notificationManager.notify(0, notificationBuilder.build())
    }
    
}