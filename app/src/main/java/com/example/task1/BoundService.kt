package com.example.task1

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import androidx.core.app.NotificationCompat
import com.example.task1.App.Companion.channelID


class BoundService : Service() {

    enum class ConnectionState {
        CONNECTED,
        DISCONNECTED,
    }

    enum class ServiceState {
        IDLE,
        BUSY
    }

    private val localBinder: IBinder = MyBinder()
    private var mConnectionState: ConnectionState = ConnectionState.DISCONNECTED

    override fun onBind(intent: Intent): IBinder? {
        return localBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent()
        connect()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle("Example Service")
            .setSmallIcon(
                when (mConnectionState) {
                    ConnectionState.CONNECTED->R.drawable.ic_thumb_up_black_24dp
                    ConnectionState.DISCONNECTED ->R.drawable.ic_thumb_down_black_24dp
                }
            )
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

    private fun connect() {
        mConnectionState = if (mConnectionState != ConnectionState.CONNECTED) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.DISCONNECTED
        }

    }

}
