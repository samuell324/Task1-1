package com.example.task1

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.task1.App.Companion.channelID
import kotlin.coroutines.coroutineContext


class BoundService : Service() {

    var mMessenger: Messenger = Messenger(IncomingHandler())

    enum class ConnectionState(i: Int) {
        CONNECTED(1),
        DISCONNECTED(1),
    }

    enum class ServiceState {
        IDLE,
        BUSY
    }

    private val localBinder: IBinder = MyBinder()
    private var mConnectionState: ConnectionState = ConnectionState.DISCONNECTED

    @SuppressLint("ShowToast")
    override fun onBind(intent: Intent): IBinder? {
        Toast.makeText(applicationContext, "Service Binding...", Toast.LENGTH_LONG)
        return mMessenger.binder
    }

    class IncomingHandler: Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ConnectionState.CONNECTED.ordinal -> ConnectionState.CONNECTED
                ConnectionState.DISCONNECTED.ordinal -> ConnectionState.DISCONNECTED
            }
            super.handleMessage(msg)
        }
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
                    ConnectionState.DISCONNECTED->R.drawable.ic_thumb_down_black_24dp
                }
            )
            .setContentText("Example")
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(startId,notification.build())

        startForeground(startId, notification.build())
        return START_NOT_STICKY
    }

    inner class MyBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }


     fun connect() {
        mConnectionState = if (mConnectionState != ConnectionState.CONNECTED) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.DISCONNECTED
        }
    }

}
