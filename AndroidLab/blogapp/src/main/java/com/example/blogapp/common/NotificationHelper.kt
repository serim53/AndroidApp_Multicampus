package com.example.blogapp.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.blogapp.receiver.MyReceiver

//Notification 을 만드는 Builder를 준비할 때
//26 이후부터는 꼭 채널 개념 적용해야 하고.. 이하는 채널 개념이 없고..
fun createNotificationChannel(context: Context) {
    //앱이 실행되는 유저폰의 버전..Build.VERSION.SDK_INT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "default_channel",
            "기본 알림",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "앱의 기본 알림 채널입니다."
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        //준비된 채널을 등록..
        manager.createNotificationChannel(channel)
    }
}

//notification 을 띄우고자 할때 호출되는 함수..
fun showSimpleNotification(context: Context){
    createNotificationChannel(context)//채널 준비해서 시스템에 등록되게 하고..

    //알림이 뜬 후 유저가 이벤트를 가한다.. 이벤트 발생시에 우리의 리시버가 실행되게 하고 싶다..
    //리시버를 실행시키기 위한 인텐트 준비한다.
    val intent = Intent(context, MyReceiver::class.java)
    //이벤트가 우리 앱에서 발생한 것이 아니다.
    //인텐트는 우리가 준비하지만 인텐트를 우리가 발생시킬 수 없다.
    //이벤트 의뢰...
    val pIntent = PendingIntent.getBroadcast(context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE)

    //알림 구성.. 등록된 채널 id 를 명시해서..
    val notification = NotificationCompat.Builder(context, "default_channel")
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle("알림")
        .setContentText("메시지 도착")
        .setContentIntent(pIntent)//이벤트 의뢰..
        .build()

    //발생....
    val manager = context.getSystemService(NotificationManager::class.java)
    manager.notify(11, notification)

}
