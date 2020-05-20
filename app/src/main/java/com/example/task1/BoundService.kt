package com.example.task1
import android.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import androidx.core.app.NotificationCompat


class BoundService : Service() {

    var mMessenger: Messenger = Messenger(IncomingHandler())
    var mConnectionState = ConnectionState.DISCONNECTED
    private val startId = 1

    companion object {
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
    }

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
        when(intent?.action) {
            ACTION_START_FOREGROUND_SERVICE -> startForegroundService()
            ACTION_STOP_FOREGROUND_SERVICE -> stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        startForeground(startId, getMyActivityNotification(smallIcon = R.drawable.btn_default_small))
    }

    private fun getMyActivityNotification(smallIcon: Int): Notification {
        updateNotification()
        val intent = Intent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent, 0)
        val b = NotificationCompat.Builder(this)
        b.setOngoing(true)
        b.setContentIntent(pendingIntent)
        b.setContentTitle("Title")
        b.setContentText("Simple text")
        b.setSmallIcon(smallIcon)
        return b.build()
    }

    private fun updateNotification() {
        val smallIcon = R.drawable.ic_dialog_info
        val notification: Notification = getMyActivityNotification(smallIcon)
        val mNotificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(startId, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }
}
