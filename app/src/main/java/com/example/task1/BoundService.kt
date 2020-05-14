package com.example.task1

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.task1.App.Companion.channelID


class BoundService : Service() {
    private val localBinder: IBinder = MyBinder()
    override fun onBind(intent: Intent): IBinder? {
        return localBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle("Example Service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("Example")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    inner class MyBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }
}
