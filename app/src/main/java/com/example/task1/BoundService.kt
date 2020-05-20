package com.example.task1
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.task1.App.Companion.channelID


class BoundService : Service() {

    var mMessenger: Messenger = Messenger(IncomingHandler())
    var mConnectionState = ConnectionState.DISCONNECTED

    enum class ConnectionState(i: Int) {
        CONNECTED(1),
        DISCONNECTED(1),
        IDLE(1),
        BUSY(1)
    }

    override fun onBind(intent: Intent): IBinder? {
        Toast.makeText(applicationContext, "Service Binding...", Toast.LENGTH_LONG).show()
        return mMessenger.binder
    }

    inner class IncomingHandler(): Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ConnectionState.DISCONNECTED.ordinal-> mConnectionState = ConnectionState.DISCONNECTED
                ConnectionState.CONNECTED.ordinal -> mConnectionState = ConnectionState.CONNECTED
                ConnectionState.IDLE.ordinal -> mConnectionState = ConnectionState.IDLE
                ConnectionState.BUSY.ordinal -> mConnectionState = ConnectionState.BUSY
            }
            super.handleMessage(msg)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, channelID)
            notification.setContentTitle("Example Service")
            notification.setContentText("Example")
            notification.setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notification.setSmallIcon(
            when(mConnectionState) {
                ConnectionState.CONNECTED -> R.drawable.ic_thumb_up_black_24dp
                ConnectionState.DISCONNECTED -> R.drawable.ic_thumb_down_black_24dp
                ConnectionState.IDLE -> R.drawable.ic_watch_later_black_24dp
                ConnectionState.BUSY -> R.drawable.ic_pan_tool_black_24dp
            }
        )
        notificationManager.notify(startId,notification.build())
        startForeground(startId, notification.build())
        return START_NOT_STICKY
    }

    /*fun connect() {
        mConnectionState = if (mConnectionState != ConnectionState.CONNECTED) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.DISCONNECTED
        }
    }*/
}
