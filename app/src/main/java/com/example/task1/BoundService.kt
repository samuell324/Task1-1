package com.example.task1
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.task1.App.Companion.channelID
import android.app.NotificationManager
import android.content.Context


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
        DISCONNECTED(2),
        IDLE(3),
        BUSY(4)
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
            updateNotification()
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
        startForeground(startId, getMyActivityNotification(smallIcon = R.drawable.ic_launcher_foreground))
    }

    private fun getMyActivityNotification(smallIcon: Int): Notification {
        val intent = Intent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent, 0)
        val b = NotificationCompat.Builder(this, channelID )
        b.setContentIntent(pendingIntent)
        b.setContentTitle("Title")
        b.setContentText("Simple text")
        b.setSmallIcon(smallIcon)
        START_NOT_STICKY
        return b.build()
    }

    private fun updateNotification() {
        val smallIcon: Int = when (mConnectionState) {
            ConnectionState.CONNECTED -> R.drawable.ic_thumb_up_black_24dp
            ConnectionState.DISCONNECTED -> R.drawable.ic_thumb_down_black_24dp
            ConnectionState.BUSY -> R.drawable.ic_watch_later_black_24dp
            ConnectionState.IDLE -> R.drawable.ic_pan_tool_black_24dp
        }
        val notification: Notification = getMyActivityNotification(smallIcon)
        val mNotificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(startId, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }
}
